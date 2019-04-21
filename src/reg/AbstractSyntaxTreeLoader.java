package reg;

import reg.factor.Factor;
import reg.factor.FactorTypeRegister;
import reg.factor.closure.Closure;
import reg.factor.operand.CharacterAtom;
import reg.factor.operand.CombinedOperand;
import reg.factor.operand.GroupOperand;
import reg.factor.operand.Operand;
import reg.factor.operator.BinaryOperator;
import reg.factor.operator.Operator;
import reg.factor.operator.UnaryOperator;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Assemble sequence factor to one factor, called CombinedOperator.
 * the final factor is the root of the abstract syntax tree.
 *
 * @author flying
 */
public class AbstractSyntaxTreeLoader {
    /**
     * stack, as a register
     */
    private Stack<Factor> stack = new Stack<>();

    /**
     * Assemble times operator.
     */
    private NumOfOccurrenceOperatorAssembler timesOpAssembler = new NumOfOccurrenceOperatorAssembler();

    /**
     * Assemble group operand.
     */
    private GroupOperandAssembler groupOperandAssembler = new GroupOperandAssembler();

    /**
     * receive next factor.
     */
    public void receive(Factor factor) throws Exception {
        if (factor == null) {
            return;
        }

        if (groupOperandAssembler.uncompleted()) {
            receive(groupOperandAssembler.receiveSegment(factor));
            return;
        }

        if (timesOpAssembler.uncompleted()) {
            receive(timesOpAssembler.receiveSegment(factor));
            return;
        }

        if (FactorTypeRegister.isOperator(factor)) {
            receiveOperator((Operator) factor);
            return;
        }

        if (FactorTypeRegister.isOperand(factor)) {
            receiveOperand((Operand) factor);
            return;
        }

        if (FactorTypeRegister.isClosure(factor)) {
            receiveClosure((Closure) factor);
            return;
        }

        throw new RuntimeException("unknow exception");
    }

    /**
     * @return root of abstract syntax tree.
     * @throws Exception if syntax tree can not be constructed.
     */
    public Operand rootOfAbstractSyntaxTree() throws Exception {
        if (stack.size() == 0) {
            return null;
        }

        if (groupOperandAssembler.uncompleted()) {
            throw new Exception("Uncompleted group operand:'" + timesOpAssembler.received() + "'");
        }

        if (timesOpAssembler.uncompleted()) {
            throw new Exception("uncompleted number of occurrence operator:'" + timesOpAssembler.received() + "'");
        }

        collapse((byte)-1);
        Factor factor = stack.peek();

        if (FactorTypeRegister.isOperand(factor)) {
            return (Operand) factor;
        }

        throw new Exception("Uncompleted operator: '" + factor.expression() + "'.");
    }

    /**
     * receive a operator.
     */
    private void receiveOperator(Operator operator) throws Exception {
        Factor prev;

        if (stack.isEmpty() || !FactorTypeRegister.isOperand(prev = stack.pop())) {
            // syntax exception
            throw new RuntimeException("no left operand before operator :'" + operator.expression() + "'");
        }

        Operand operand = (Operand) prev;

        if (operator.unary()) {
            receive(new CombinedOperand(operand, operator));
            return;
        }

        // binary operator, put into stack waiting for next right operator.
        stack.push(operand);
        collapse(operator.precedence());
        stack.push(operator);
        return;
    }

    /**
     * Receive a operand
     */
    private void receiveOperand(Operand operand) throws Exception {
        if (stack.isEmpty()) {
            // start of the expression or sub-expression
            stack.push(operand);
            return;
        }

        Factor prev = stack.peek();

        if (FactorTypeRegister.isClosure(prev) || FactorTypeRegister.isOperator(prev)) {
            stack.push(operand);
            return;
        }

        if (FactorTypeRegister.isOperand(prev)) {
            // connect prev and factor with a connection operator.
            receive(BinaryOperator.CONNECT);
            stack.push(operand);
            return;
        }

        // not reachable code
        throw new RuntimeException("unknow exception");
    }

    private void receiveClosure(Closure closure) throws Exception {
        // if closure is bracket('[', '[^' or ']'), treat it as segment of a number of occurrence operator.
        if (FactorTypeRegister.instanceOf(closure.type(), FactorTypeRegister.BRACKET_MASK) ||
            FactorTypeRegister.instanceOf(closure.type(), FactorTypeRegister.ANTI_BRACKET_MASK))
        {
            if (!closure.left()) {
                throw new RuntimeException("Can not found a valid corresponding symbol before:'" +
                    closure.expression() + "'");
            }

            groupOperandAssembler.receiveSegment(closure);
            return;
        }

        // if closure is brace('{' or '}'), treat it as segment of a number of occurrence operator.
        if (FactorTypeRegister.instanceOf(closure.type(), FactorTypeRegister.BRACE_MASK)) {
            if (!closure.left()) {
                throw new RuntimeException("Can not found a valid corresponding symbol before:'" +
                    closure.expression() + "'");
            }

            timesOpAssembler.receiveSegment(closure);
            return;
        }

        // try collapsing the factor has received before.
        collapse((byte)-1);

        if (closure.left()) {
            // be the start symbol of a sub-expression
            stack.push(closure);
            return;
        }

        // find corresponding closure, which must be valid.
        if (stack.isEmpty()) {
            throw new RuntimeException("Can not found a valid corresponding symbol before:'" +
                closure.expression() + "'");
        }

        Factor prev = stack.pop();

        if (!FactorTypeRegister.isOperand(prev)) {
            // syntax error
            throw new RuntimeException("The factor before closure:'" + closure.expression() + "' is " +
                prev.expression());
        }

        Operand operand = (Operand) prev;

        if (stack.isEmpty()) {
            // syntax error
            throw new RuntimeException("Closure : '" + closure.expression() + "' can not find corresponding symbol:'" +
                closure.corresponding() + "'");
        }

        Closure corresponding = (Closure) stack.pop();

        if (!closure.match(corresponding)) {
            throw new RuntimeException("Expect a corresponding symbol:'" + closure.corresponding() + "' before '" +
                closure.expression() + "', but found a '" + corresponding.expression() + "', they are not a valid pair.");
        }

        receive(new CombinedOperand(corresponding, operand, closure));
    }

    /**
     * combine prev operand and prev-of-prev operand.
     * @param precedence, if the precedence less than operator's which before prev operand, then combined prev operand
     * and prev-of-prev operand.
     * @return true means some prev factors had been combined.
     */
    private boolean collapse(byte precedence) throws Exception {
        if (stack.size() < 3) {
            return false;
        }

        Factor last = stack.pop();
        Factor secondToLast = stack.pop();
        Factor thirdToLast = stack.pop();

        // left-operand operator right-operand
        if (!FactorTypeRegister.isOperand(last) || !FactorTypeRegister.isOperand(thirdToLast) ||
            !FactorTypeRegister.isOperator(secondToLast) || precedence > ((Operator) secondToLast).precedence() )
        {
            stack.push(thirdToLast);
            stack.push(secondToLast);
            stack.push(last);
            return false;
        }

        Operand leftOperand = (Operand) thirdToLast;
        Operator operator = (Operator) secondToLast;
        Operand rightOperand = (Operand) last;
        stack.push(new CombinedOperand(leftOperand, operator, rightOperand));
        collapse(precedence);
        return true;
    }

    /**
     * assembler fot assemble {@link UnaryOperator.NumOfOccurrence} from sequence of fragments.
     */
    private static class NumOfOccurrenceOperatorAssembler {
        /**
         * separator of min times and max times in a number of occurrence operator.
         */
        private static final short TIMES_SEPARATOR = (short) ',';

        /**
         * true means assembler has received a '{'.
         */
        private boolean started;

        /**
         * true means assembler has received a time separator.
         */
        private boolean hasReceivedTimeSeparator = false;

        /**
         * min of range
         */
        private int min;

        /**
         * mix of range
         */
        private int max = Integer.MAX_VALUE;

        /**
         * register, used to remember sequence fragments has received.
         */
        private ArrayList<CharacterAtom> fragments = new ArrayList<>();

        /**
         * receive segment of group operand.
         *
         * @return null if sequencing received fragments can not combined to a {@link UnaryOperator.NumOfOccurrence} yet,
         * or return {@link UnaryOperator.NumOfOccurrence} instance if receive a right closure and
         * the fragment has received can assembled to a {@link UnaryOperator.NumOfOccurrence}.
         *
         * @throws Exception if received fragments can never combined to a {@link UnaryOperator.NumOfOccurrence}.
         */
        public UnaryOperator.NumOfOccurrence receiveSegment(Factor factor) throws Exception {
            if (factor == null) {
                throw new NullPointerException("Param 'factor' is null.");
            }

            if (Closure.Brace.LEFT.equals(factor.expression())) {
                if (started) {
                    throw new RuntimeException("NumOfOccurrence operator must start with '" + Closure.Brace.LEFT + "'");
                }

                started = true;
                return null;
            }

            if (Closure.Brace.RIGHT.equals(factor.expression())) {
                // construct number of occurrence operator
                UnaryOperator.NumOfOccurrence times = new UnaryOperator.NumOfOccurrence(min, max);
                reset();
                return times;
            }

            if (FactorTypeRegister.isOperand(factor) &&
                FactorTypeRegister.instanceOf(factor.type(), FactorTypeRegister.CHARACTER_ATOM))
            {
                CharacterAtom operand = (CharacterAtom) factor;
                fragments.add(operand);
                short character = operand.character();

                if (character == TIMES_SEPARATOR) {
                    if (hasReceivedTimeSeparator) {
                        throw new RuntimeException("at near:'" + received() + "'");
                    }

                    hasReceivedTimeSeparator = true;
                    return null;
                }

                // 0 ~ 9
                if (character < 48 || character > 57) {
                    throw new RuntimeException("at near '" + received() + "'");
                }

                int number = Integer.valueOf(factor.expression());

                if (!hasReceivedTimeSeparator) {
                    min = min * 10 +  number;
                } else {
                    max = (max == Integer.MAX_VALUE) ? number : (max * 10 +  number);
                }

                return null;
            }

            throw new RuntimeException("at near '" + received() + factor.expression() + "'");
        }

        /**
         * @return true means the assembler is incompletely.
         */
        public boolean uncompleted() {
            return started;
        }

        /**
         * String of factors has received.
         */
        public String received() {
            if (!started) {
                return "";
            }

            StringBuilder result = new StringBuilder(Closure.Brace.LEFT);

            for (Factor factor : fragments) {
                result.append(factor.expression());
            }

            return result.toString();
        }

        /**
         * reset state.
         */
        private void reset() {
            fragments.clear();
            hasReceivedTimeSeparator = false;
            started = false;
            min = 0;
            max  = Integer.MAX_VALUE;
        }
    }

    /**
     * Assembler, assemble {@link GroupOperand} from sequence of fragments.
     */
    private static class GroupOperandAssembler {
        /**
         * true means group is anti-mode.
         */
        private Boolean antiMode = null;

        /**
         * register, used to remember sequence fragments assembler has received.
         */
        private ArrayList<CharacterAtom> fragments = new ArrayList<>();

        /**
         * receive segment of group operand.
         *
         * @return null if sequencing received fragments can not combined to a {@link GroupOperand} yet,
         * or return {@link GroupOperand} instance if receive a right closure and
         * the fragment has received can assembled to a {@link GroupOperand}.
         *
         * @throws Exception if received fragments can never combined to a {@link GroupOperand}.
         */
        public GroupOperand receiveSegment(Factor factor) throws Exception {
            if (factor == null) {
                throw new NullPointerException("Param 'factor' is null.");
            }

            if (Closure.Bracket.LEFT.equals(factor.expression()) ||
                Closure.AntiBracket.LEFT.equals(factor.expression()))
            {
                if (antiMode != null) {
                    throw new RuntimeException("Character '" + factor.expression() + "' is offend after :'" +
                        received() + "'");
                }

                antiMode = Closure.AntiBracket.LEFT.equals(factor.expression());
                return null;
            }

            if (Closure.Bracket.RIGHT.equals(factor.expression())) {
                // construct group
                if (fragments.size() == 3 && GroupOperand.RANGE_SEPARATOR == fragments.get(1).character()) {
                    // is range
                    CharacterAtom min = fragments.get(1);
                    CharacterAtom max = fragments.get(3);

                    if (min.special() || max.special()) {
                        throw new RuntimeException("The group '" + received() + "' is invalid, limit of range must be" +
                            " common character. ");
                    }

                    GroupOperand result = new GroupOperand(antiMode, min, max);
                    reset();
                    return result;
                }

                GroupOperand result = new GroupOperand(antiMode, fragments.toArray(new CharacterAtom[fragments.size()]));
                reset();
                return result;
            }

            if (FactorTypeRegister.isOperand(factor) &&
                FactorTypeRegister.instanceOf(factor.type(), FactorTypeRegister.CHARACTER_ATOM))
            {
                fragments.add((CharacterAtom) factor);
                return null;
            }

            throw new RuntimeException("at near: '" + received() + factor.expression() + "'");
        }

        /**
         * @return true means the assembler in incompletely.
         */
        public boolean uncompleted() {
            return antiMode != null;
        }

        /**
         * String of factors assembler has received.
         */
        public String received() {
            if (antiMode == null) {
                return "";
            }

            StringBuilder result = new StringBuilder(antiMode ? Closure.AntiBracket.LEFT : Closure.Bracket.LEFT);

            for (Factor factor : fragments) {
                result.append(factor.expression());
            }

            return result.toString();
        }

        /**
         * reset state.
         */
        private void reset() {
            fragments.clear();
            antiMode = null;
        }
    }
}
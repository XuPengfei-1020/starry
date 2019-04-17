package reg;

import reg.factor.Factor;
import reg.factor.closure.Closure;
import reg.factor.operand.CharacterAtom;
import reg.factor.operand.CombinedOperand;
import reg.factor.operand.Operand;
import reg.factor.operator.BinaryOperator;
import reg.factor.operator.Operator;
import reg.factor.operator.UnaryOperator;

import java.util.LinkedList;
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
     * receive next factor.
     */
    public void receive(Factor factor) throws Exception {
        if (factor == null) {
            return;
        }

        if (timesOpAssembler.uncompleted()) {
            UnaryOperator.NumOfOccurrence numOfOccurrenceOp = timesOpAssembler.receiveTimesSegment(factor);

            if (numOfOccurrenceOp != null) {
                receive(numOfOccurrenceOp);
            }

            return;
        }

        if (factor.isOperator()) {
            Factor prev;

            if (stack.isEmpty() || !(prev = stack.pop()).isOperand()) {
                // syntax exception
                throw new RuntimeException("No left operand before operator :'" + factor.letter() + "'");
            }

            Operand operand = (Operand) prev;
            Operator operator = (Operator) factor;

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

        if (factor.isOperand()) {
            if (stack.isEmpty()) {
                // start of the expression or sub-expression
                stack.push(factor);
                return;
            }

            Factor prev = stack.peek();

            if (prev.isClosure() || prev.isOperator()) {
                stack.push(factor);
                return;
            }

            if (prev.isOperand()) {
                // connect prev and factor with a connection operator.
                receive(BinaryOperator.CONNECT);
                stack.push(factor);
                return;
            }

            // not reachable code
            throw new RuntimeException("unknow exception");
        }

        if (factor.isClosure()) {
            Closure closure = (Closure) factor;

            // if closure is brace('{' or '}'), treat it as segment of a number of occurrence operator.
            if (closure instanceof Closure.Brace) {
                if (closure.left()) {
                    timesOpAssembler.receiveTimesSegment(closure);
                    return;
                }

                throw new RuntimeException("Can not found a valid corresponding symbol before:'" + factor.letter() + "'");
            }

            collapse(-1);

            if (closure.left()) {
                // be the start symbol of a sub-expression
                stack.push(factor);
                return;
            }

            // find corresponding closure, which must be valid.
            if (stack.isEmpty()) {
                throw new RuntimeException("Can not found a valid corresponding symbol before:'" + factor.letter() +
                    "'");
            }

            Factor prev = stack.pop();

            if (!prev.isOperand()) {
                // syntax error
                throw new RuntimeException("The factor before closure:'" + factor.letter() + "' is " + prev.letter());
            }

            Operand operand = (Operand) prev;

            if (stack.isEmpty()) {
                // syntax error
                throw new RuntimeException("Closure : '" + closure.letter() + "' can not find corresponding symbol:'" +
                    closure.corresponding() + "'");
            }

            Closure corresponding = (Closure) stack.pop();

            if (!closure.match(corresponding)) {
                throw new RuntimeException("Expect a corresponding symbol:'" + closure.corresponding() + "' before '" +
                    closure.letter() + "', but found a '" + corresponding.letter() + "', they are not a valid pair.");
            }

            receive(new CombinedOperand(corresponding, operand, closure));
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

        if (timesOpAssembler.uncompleted()) {
            throw new Exception("Uncompleted number of occurrence operator:：" + timesOpAssembler.fragments());
        }

        collapse(-1);
        Factor factor = stack.peek();

        if (factor.isOperand()) {
            return (Operand) factor;
        }

        throw new Exception("Uncompleted operator: '" + factor.letter() + "'.");
    }

    /**
     * combine prev operand and prev-of-prev operand.
     * @param precedence, if the precedence less than operator's which before prev operand, then combined prev operand
     * and prev-of-prev operand.
     * @return true means some prev factors had been combined.
     */
    private boolean collapse(int precedence) throws Exception {
        if (stack.size() < 3) {
            return false;
        }

        Factor last = stack.pop();
        Factor sceondToLast = stack.pop();
        Factor thirdToLast = stack.pop();

        // left-operand operator right-operand
        if (!last.isOperand() || !thirdToLast.isOperand() || !sceondToLast.isOperator() ||
            precedence > ((Operator) sceondToLast).precedence() )
        {
            stack.push(thirdToLast);
            stack.push(sceondToLast);
            stack.push(last);
            return false;
        }

        Operand leftOperand = (Operand) thirdToLast;
        Operator operator = (Operator) sceondToLast;
        Operand rightOperand = (Operand) last;
        stack.push(new CombinedOperand(leftOperand, operator, rightOperand));
        collapse(precedence);
        return true;
    }

    private static class NumOfOccurrenceOperatorAssembler {
        /**
         * separator of min times and max times in a number of occurrence operator.
         */
        private static final int timesSeparator = ',';

        /**
         * register, used to remember sequence fragments of number of occurrence operator.
         */
        private LinkedList<Factor> fragments = new LinkedList<>();

        /**
         * left side of range.
         */
        private int min = 0;

        /**
         * right side of range
         */
        private int max = Integer.MAX_VALUE;

        /**
         * State for number of occurrence operator construction
         */
        private Step step = new Step();

        /**
         * receive segment of number of occurrence operator.
         *
         * @return null if sequencing received fragments can not combined to a number of occurrence operator yet,
         * or return {@link UnaryOperator.NumOfOccurrence} if receive a right closure and
         * the state closure can combine the fragments before received to a correct number of occurrence operator.
         *
         * @throws Exception if received fragments can never combined to a correct number of occurrence operator.
         */
        public UnaryOperator.NumOfOccurrence receiveTimesSegment(Factor factor) throws Exception {
            if (factor == null) {
                throw new NullPointerException("Param 'factor' is null.");
            }

            fragments.push(factor);

            if (step.currentStep == Step.START) {
                if (Closure.Brace.LEFT.equals(factor.letter())) {
                    step.currentStep = Step.LEFT_BRACE;
                    return null;
                }

                throw new RuntimeException("NumOfOccurrence operator must start with '" + Closure.Brace.LEFT + "'");
            }

            if (Closure.Brace.RIGHT.equals(factor.letter())) {
                if (step.currentStep != Step.TIMES_SEPARATOR && step.currentStep != Step.MAX) {
                    throw new RuntimeException("Unrecognized number of occurrence operator '" + fragments() + "'");
                }

                // construct number of occurrence operator
                UnaryOperator.NumOfOccurrence times = new UnaryOperator.NumOfOccurrence(min, max);
                reset();
                return times;
            }

            if (factor.isOperand() && factor instanceof CharacterAtom) {
                CharacterAtom operand = (CharacterAtom) factor;
                int character = operand.character();

                if (character == timesSeparator) {
                    if (step.currentStep != Step.LEFT_BRACE && step.currentStep != Step.MIN) {
                        throw new RuntimeException("Unrecognized number of occurrence operator '" + fragments() + "'");
                    }

                    step.currentStep = Step.TIMES_SEPARATOR;
                    return null;
                }

                // 0 ~ 9
                if (character >= 48 && character <= 57) {
                    int number = Integer.valueOf(factor.letter());

                    if (step.currentStep == Step.LEFT_BRACE) {
                        min = number;
                        step.currentStep = Step.MIN;
                        return null;
                    }

                    if (step.currentStep == Step.MIN) {
                        min = min * 10 +  number;
                        return null;
                    }

                    if (step.currentStep == Step.TIMES_SEPARATOR) {
                        max = number;
                        step.currentStep = Step.MAX;
                        return null;
                    }

                    if (step.currentStep == Step.MAX) {
                        max = max * 10 +  number;
                        return null;
                    }
                }
            }

            throw new RuntimeException("Unrecognized number of occurrence operator '" + fragments() + "'");
        }

        /**
         * @return true means the assembler has a incomplete number of occurrence operator.
         */
        public boolean uncompleted() {
            return step.currentStep != 0;
        }

        /**
         * Fragments to String.
         */
        public String fragments() {
            StringBuilder result = new StringBuilder();

            for (Factor factor : fragments) {
                result.insert(0, factor.letter());
            }

            return result.toString();
        }

        /**
         * reset state.
         */
        private void reset() {
            fragments.clear();
            min = 0;
            max = Integer.MAX_VALUE;
            step.currentStep = 0;
        }

        /**
         * Step for number of occurrence operator construction
         */
        private static class Step {
            /**
             * The follows state are step for number of occurrence operator construction.
             */
            public static final int START = 0;
            public static final int LEFT_BRACE = 1;
            public static final int MIN = 2;
            public static final int TIMES_SEPARATOR = 3;
            public static final int MAX = 4;
            public static final int RIGHT_BRACE = 5;

            /**
             * Current step
             */
            public int currentStep = 0;
        }
    }
}
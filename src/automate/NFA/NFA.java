package automate.NFA;

import automate.FinteAutomate;
import automate.state.nfa.NFAState;
import automate.state.State;
import automate.transition.DefaultMatchRange;
import automate.transition.EpsilonTranstion;
import automate.transition.RangeRuleTransition;
import reg.factor.FactorTypeRegister;
import reg.factor.operand.CharacterAtom;
import reg.factor.operand.CombinedOperand;
import reg.factor.operand.GroupOperand;
import reg.factor.operand.Operand;
import reg.factor.operator.BinaryOperator;
import reg.factor.operator.Operator;
import reg.factor.operator.UnaryOperator;

import java.util.Collection;
import java.util.HashSet;

/**
 * An NFA instance present a NFA.
 *
 * @author flying
 */
public class NFA implements FinteAutomate {
    /** start and accept state **/
    private NFAState start;
    private NFAState accept;

    /**
     * Constructor
     * @param operand abstract-syntax-tree
     */
    public NFA(Operand operand) {
        if (operand == null) {
            throw new NullPointerException("Param 'operand' is null!");
        }

        if (FactorTypeRegister.instanceOf(operand.type(), FactorTypeRegister.CHARACTER_ATOM)) {
            // build a simple NFA (START ---- c ----> ACCEPT)
            NFAState start = new NFAState();
            NFAState accept = new NFAState();
            short character = ((CharacterAtom)operand).character();
            start.connect(new RangeRuleTransition(new DefaultMatchRange(character, character)), accept);
            this.start = start;
            this.accept = accept;
            return;
        }

        if (FactorTypeRegister.instanceOf(operand.type(), FactorTypeRegister.GROUP_OPERAND)) {
            // treat group as a simple operand, then build a simple NFA (START ---- transition ----> ACCEPT)
            // the transition of this NFA is little more complex than simple transition,
            // it depending in how complex group is.
            NFAState start = new NFAState();
            NFAState accept = new NFAState();
            GroupOperand.HashGroup group = (GroupOperand.HashGroup) operand;
            CharacterAtom[] members = group.getMembers();
            DefaultMatchRange[] matchRanges = new DefaultMatchRange[members.length];

            // @todo, do sth work to improve performance.
            for (int i = 0; i < members.length; i++) {
                matchRanges[i] = new DefaultMatchRange(members[i].character(), members[i].character());
            }

            start.connect(new RangeRuleTransition(matchRanges, group.isExcludeMode()), accept);
            this.start = start;
            this.accept = accept;

            return;
        }

        if (FactorTypeRegister.instanceOf(operand.type(), FactorTypeRegister.RANGE_GROUP_OPERAND)) {
            // treat group as a simple operand, then build a simple NFA (START ---- transition ----> ACCEPT)
            // the transition of this NFA is little more complex than simple transition,
            // it depending in how complex group is.
            NFAState start = new NFAState();
            NFAState accept = new NFAState();
            GroupOperand.PartitionGroup group = (GroupOperand.PartitionGroup) operand;
            start.connect(
                new RangeRuleTransition(new DefaultMatchRange(group.from(), group.to()), group.isExcludeMode()), accept);
            this.start = start;
            this.accept = accept;
            return;
        }

        if (FactorTypeRegister.instanceOf(operand.type(), FactorTypeRegister.COMBINED_OPERAND)) {
            CombinedOperand combinedOperand = (CombinedOperand)operand;
            Operator operator = combinedOperand.getOperator();
            NFA left = new NFA(combinedOperand.getLeftOperand());

            if (operator == null) {
                this.start = left.start;
                this.accept = left.accept;
                return;
            }


            NFA right = combinedOperand.getRightOperand() == null ? null : new NFA(combinedOperand.getRightOperand());

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.CONNECT) ) {
                // connect operator, combined accept of left and start of right.
                if (left.accept == null || right.start == null) {
                    System.out.printf("null");
                }
                left.accept.combine(right.start);
                this.start = left.start;
                this.accept = right.accept;
                return;
            }

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.OR) ) {
                // or operator,
                // 1. combined tow start state of both left and right if any of them has any in-path,
                //     else use a new start state connect them with ε-transition.
                // 2.then combined tow accept state of both left and right if any of them has any out-path,
                //     else connect both of them to a new accept state with ε-transition.
                if (!left.start.prevs().isEmpty() || !right.start.prevs().isEmpty()) {
                    NFAState newStart = new NFAState();
                    newStart.connect(new EpsilonTranstion(), left.start);
                    newStart.connect(new EpsilonTranstion(), right.start);
                    this.start = newStart;
                } else {
                    left.start.combine(right.start);
                    this.start = left.start;
                }

                if (!left.accept.nexts().isEmpty() || !right.accept.nexts().isEmpty()) {
                    NFAState newAccept = new NFAState();
                    left.accept.connect(new EpsilonTranstion(), newAccept);
                    right.accept.connect(new EpsilonTranstion(), newAccept);
                    this.accept = newAccept;
                } else {
                    left.accept.combine(right.accept);
                    this.accept = left.accept;
                }

                return;
            }

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.STAR) ) {
                // start operator, connect start with accept use tow ε-transitions，
                // one of them is from accept to start, and the other is opposite.
                left.start.connect(new EpsilonTranstion(), left.accept);
                left.accept.connect(new EpsilonTranstion(), left.start);
                this.start = left.start;
                this.accept = left.accept;
                return;
            }

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.AT_LAST_ONCE) ) {
                // at_last_once, connect accept with start in transition from accept to start.
                left.accept.connect(new EpsilonTranstion(), left.start);
                this.start = left.start;
                this.accept = left.accept;
                return;
            }

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.AT_MOST_ONCE) ) {
                // at_most_once, connect start with accept in transition from start to accept.
                left.start.connect(new EpsilonTranstion(), left.accept);
                this.start = left.start;
                this.accept = left.accept;
                return;
            }

            if (FactorTypeRegister.instanceOf(operator.type(), FactorTypeRegister.NUM_OF_OCCURRENCE) ) {
                // do some translate work by rules: a{3, 6} ==> aaa(a(a(a)?)?)?
                // then process them use the rules of others format.
                NFA nfa = new NFA(trnaslate((CombinedOperand) operand));
                this.start = nfa.start;
                this.accept = nfa.accept;
                return;
            }

            throw new RuntimeException("Unreached code");
        }
    }

    @Override
    public State start() {
        return start;
    }

    @Override
    public Collection<State> accepts() {
        HashSet<State> accepts = new HashSet();
        accepts.add(accept);
        return accepts;
    }

    @Override
    public boolean match(String text) {
        return false;
    }

    /**
     * Translate combinedOperand witch operator is {@link UnaryOperator.NumOfOccurrence} to format
     * easier to convert to NFA. eg: a{3, 6} to aaa(a(a(a)?)?)?
     * @param operand combinedOperand witch operator is {@link UnaryOperator.NumOfOccurrence}
     * @return CombinedOperator which consist of operand and {@link BinaryOperator.Connect},
     * {@link UnaryOperator.AtMostOnce}
     */
    private Operand trnaslate(CombinedOperand operand) {
        if (!FactorTypeRegister.instanceOf(operand.getOperator().type(), FactorTypeRegister.NUM_OF_OCCURRENCE)) {
            return operand;
        }

        Operand left  = operand.getLeftOperand();
        UnaryOperator.NumOfOccurrence operator = (UnaryOperator.NumOfOccurrence)operand.getOperator();
        int n = operator.n();
        Operand newLeft = null;

        if (n > 0) {
            newLeft = left;

            while (n-- > 1) {
                newLeft = new CombinedOperand(newLeft, BinaryOperator.CONNECT, left);
            }
        }

        Operand newRight = null;

        if (operator.m() == Integer.MAX_VALUE) {
            newRight = new CombinedOperand(left, UnaryOperator.STAR);
        } else {
            int m = operator.m() - operator.n();

            if (m > 0) {
                newRight = new CombinedOperand(left, UnaryOperator.AT_MOST_ONCE);

                while (m-- > 1) {
                    newRight = new CombinedOperand(new CombinedOperand(left, BinaryOperator.CONNECT, newRight),
                        UnaryOperator.AT_MOST_ONCE);
                }
            }
        }

        if (newLeft == null) {
            return newRight;
        }

        if (newRight == null) {
            return newLeft;
        }

        return new CombinedOperand(newLeft, BinaryOperator.CONNECT, newRight);
    }
}
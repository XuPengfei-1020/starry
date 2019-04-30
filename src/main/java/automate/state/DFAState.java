package automate.state;

import automate.transition.MatchRange;
import automate.transition.Transition;

import java.util.BitSet;
import java.util.Collection;

/**
 * A DFAStates instance present a state for DFA.
 *
 * @author flying
 */
public class DFAState extends AbstractState<DFAState> {
    /**
     * remember distribution of ranges.
     * for judge if a gavin range intersecting with exists ranges.
     */
    private BitSet rangeRegister = new BitSet();

    @Override
    public void connect(Transition transition, DFAState state) {
        if (transition == null || state == null) {
            throw new IllegalArgumentException("Param can not be null");
        }

        if (checkRangeIntersecting(transition)) {
            throw new RuntimeException("Ranges in transition that will be added is intersecting with existing");
        }

        registerRange(transition);
        super.connect(transition, state);
    }

    /**
     * Transition to next state.
     * @param c gavin character
     * @return next state which can be arrived by the gavin character specified by {@param c}
     */
    public DFAState transferTo(short c) {
        return transferTo(c, c);
    }

    /**
     * Transition to next state.
     * @param from left of range
     * @param to right of range
     * @return next state which can be arrived by the gavin character range specified by {@param from and to}
     */
    public DFAState transferTo(short from, short to) {
        Collection<DFAState> nexts = transfer(from, to);

        if (nexts.size() == 0) {
            return null;
        }

        if (nexts.size() == 1) {
            return nexts.iterator().next();
        }

        throw new RuntimeException("There has more than one next state can be arrived by same transition.");
    }

    /**
     * check if ranges in gavin transition is intersecting with this's.
     * true means there has at last tow ranges intersecting with each other.
     */
    private boolean checkRangeIntersecting(Transition t) {
        for (MatchRange range : t.matchRanges()) {
            for (int from  = range.from(); from <= range.to(); from++) {
                if (rangeRegister.get(from + Short.MAX_VALUE + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * register ranges.
     */
    private void registerRange(Transition t) {
        for (MatchRange range : t.matchRanges()) {
            rangeRegister.set(range.from() + Short.MAX_VALUE + 1, range.to() + Short.MAX_VALUE + 1);
        }
    }
}
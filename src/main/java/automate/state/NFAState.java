package automate.state;

import automate.transition.EpsilonTranstion;
import automate.transition.Transition;

import java.util.*;

/**
 * Abstract state.
 * @author flying
 */
public class NFAState extends AbstractState<NFAState> {
    /**
     * Combine the gavin state to this. all transitions linked to gavin state will redirect to this.
     * all transitions extending from the gavin state will linked to this as the extending transition.
     * @param state gavin state, will be not modified
     */
    public void combine(NFAState state) {
        Map<Transition, State> prevs = new HashMap<>(state.prevs());

        for (Map.Entry<Transition, State> entry : prevs.entrySet()) {
            State prev = entry.getValue();
            Transition transition = entry.getKey();
            prev.disconnect(transition, state);
            prev.connect(transition, this);
        }

        Map<Transition, NFAState> nexts = state.nexts();

        for (Map.Entry<Transition, NFAState> entry : nexts.entrySet()) {
            NFAState next = entry.getValue();
            Transition transition = entry.getKey();
            connect(transition, next);
        }
    }

    /**
     * @return the all states which can be arrived by ε-transition from this.
     */
    public Collection<NFAState> nextEpsilonState() {
        // todo, optimized.
        Set<NFAState> hashSet = new HashSet<>();

        for (Map.Entry<Transition, NFAState> entry : this.nexts().entrySet()) {
            if (entry.getKey() instanceof EpsilonTranstion) {
                hashSet.add(entry.getValue());
            }
        }

        return hashSet;
    }

    @Override
    public String toString() {
        return "id:" + id;
    }
}
package automate.state;

import automate.transition.EpsilonTranstion;
import automate.transition.Transition;

import java.util.*;

/**
 * Abstract state.
 * @author flying
 */
public class NFAState extends AbstractState {
    /**
     * Combine the gavin state to this. all transitions linked to gavin state will redirect to this.
     * all transitions extending from the gavin state will linked to this as the extending transition.
     * @param state gavin state, will be not modified
     */
    public void combine(State state) {
        Map<Transition, State> prevs = new HashMap<>(state.prevs());

        for (Map.Entry<Transition, State> entry : prevs.entrySet()) {
            State prev = entry.getValue();
            Transition transition = entry.getKey();
            prev.disconnect(transition, state);
            prev.connect(transition, this);
        }

        Map<Transition, State> nexts = state.nexts();

        for (Map.Entry<Transition, State> entry : nexts.entrySet()) {
            State next = entry.getValue();
            Transition transition = entry.getKey();
            connect(transition, next);
        }
    }

    /**
     * @return the all states which can be arrived by ε-transition from this.
     */
    public Collection<State> nextEpsilonState() {
        // todo, optimized.
        Set<State> hashSet = new HashSet<>();

        for (Map.Entry<Transition, State> entry : this.nexts().entrySet()) {
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
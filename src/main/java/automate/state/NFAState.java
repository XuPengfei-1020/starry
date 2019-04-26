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
     * Empty set, unmodifiable.
     */
    Set<State> empty = Collections.EMPTY_SET;

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
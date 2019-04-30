package automate.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * A SubSetDFAState instance present a DFAState which based on a sub-set state of NFA.
 *
 * @author flying
 */
public class SubSetDFAState extends DFAState {
    /**
     * ids of subset of nfa,
     */
    private HashMap<Integer, State> subSet = new HashMap<>();

    /**
     * id for subSet
     */
    private String subSetId;

    /**
     * Construct from a set of nfa state.
     * @param states a set of nfa state.
     */
    public SubSetDFAState(Collection<State> states) {
        super();

        if (states == null || states.size() == 0) {
            throw new IllegalArgumentException("Subset is null or empty");
        }

        for (State state : states) {
            subSet.put(state.id(), state);
        }

        subSetId = idForSubset(states);
    }

    /**
     * @return id of subset.
     */
    public String subSetId() {
        return subSetId;
    }

    /**
     * @return subset
     */
    public Collection<State> subSet() {
        return this.subSet.values();
    }

    /**
     * @param state the NFAState
     * @return true means this state is consist of the state specified by first param.
     */
    public boolean contanins(State state) {
        return containsNFAState(state.id());
    }

    /**
     * @param stateId id of the state's
     * @return True means this state is consist of the state which id is that specified by first param.
     */
    public boolean containsNFAState(int stateId) {
        return subSet.containsKey(stateId);
    }

    /**
     * create id for a set of state.
     * @param states a set of state.
     * @return id of set of state.
     */
    private  String idForSubset(Collection<State> states) {
        ArrayList<State> ids = new ArrayList<>(states);
        Collections.sort(ids, (State s1, State s2) -> s1.id() == s2.id() ? 0 : s1.id() > s2.id() ? 1 : -1);
        StringBuilder id = new StringBuilder("id:");

        for (State state : ids) {
            id.append(state.id()).append("-");
        }

        return id.toString();
    }
}
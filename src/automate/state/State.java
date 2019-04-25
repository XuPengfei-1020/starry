package automate.state;

import automate.transition.Transition;

import java.util.Collection;
import java.util.Map;

/**
 * state in automate.
 */
public interface State {
    /**
     * id of state
     */
    long id();

    /**
     * Connect to a state
     * @param transition the transition witch connect this state and the gavin state.
     * @param state
     */
    void connect(Transition transition, State state);

    /**
     * Disconnect with a state.
     * @param transition the transition witch connected with this state and the gavin state.
     * @param state
     */
    void disconnect(Transition transition, State state);

    /**
     * @param c, a range of character.
     * @return next states that can move to from this state by the gavin char.
     */
    Collection<State> transfer(short c);

    /**
     * @return the next state which can be arrived by  gavin transition: {@param t} .
     */
    State next(Transition t);

    /**
     * @return all pairs of transitions and states which each state can arrive this by the corresponding transition
     * and the states is all the previous state for this.
     */
    Map<Transition, State> prevs();

    /**
     * @return all pairs of transitions and states which each state can be arrived by the corresponding transition
     *  and the states is all the next state for this.
     */
    Map<Transition, State> nexts();

    /**
     * Combine the gavin state to this. all transitions linked to gavin state will redirect to this.
     * all transitions extending from the gavin state will linked to this as the extending transition.
     * @param state gavin state, will be not modified
     */
    void combine(State state);
}
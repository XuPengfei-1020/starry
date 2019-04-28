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
    int id();

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
     * @param from, to, a range of character.
     * @return next states that can move to from this state by the gavin char range.
     */
    Collection<State> transfer(short from, short to);

    /**
     * @param c, a character.
     * @return next states that can move to from this state by the gavin char.
     */
    Collection<State> transfer(short c);

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
}
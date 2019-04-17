package automate;

/**
 * state in automate.
 */
public interface State {
    /**
     * id of state
     */
    int id();

    /**
     * @return true means this state is a start state of a automate.
     */
    boolean isStart();

    /**
     * @return true means this state is a accept state of a automate.
     */
    boolean isAccept();

    /**
     * @param c a character.
     * @return next states that can move to from this state by the gavin char.
     */
    State[] next(char c);
}
package automate.transition;

/**
 * An instance of Transition is a transition of automate.
 * Which can drive current state from one to another by gavin letter.
 *
 * @author flying
 */
public interface Transition<T extends Transition> {
    /**
     * test this transition with character by the {@param c}
     * @param c the gavin character.
     * @return true means this transition can be passed by the gavin character.
     */
    boolean match(short c);
}
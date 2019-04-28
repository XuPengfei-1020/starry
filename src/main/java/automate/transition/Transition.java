package automate.transition;

import java.util.Collection;

/**
 * An instance of Transition is a transition of automate.
 * Which can drive current state from one to another by gavin letter.
 *
 * @author flying
 */
public interface Transition {
    /**
     * test this transition with character by the {@param c}
     * @param c the gavin character.
     * @return true means this transition can be passed by the gavin character.
     */
    boolean match(short c);

    /**
     * test this transition with character range by the {@param from, to}
     * @param from, to. the gavin character range.
     * @return true means this transition can be passed by the gavin character range.
     */
    boolean match(short from, short to);

    /**
     * return the range of character can be matched by this.
     * there maybe has more than one range.
     */
    Collection<MatchRange> matchRanges();
}
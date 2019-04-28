package automate.transition;

/**
 * A range of a set of characters that all can match one or more AtomOperand in a regular expression.
 * example:
 * [min, max] can match '.' in regular expression.
 * [97, 97] can match '.' in regular expression.
 *
 * @author flying
 */
public interface MatchRange {
    /**
     * left border of range
     */
    short from();

    /**
     * right border of range
     */
    short to();
}
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

    /**
     * judge if a range can concat with this range seamlessly.
     * @return true means the gavin range can concat with this range seamlessly.
     */
    boolean seamlessWith(MatchRange range);

    /**
     * Combine tow range to one. the second range must can be concat with this range seamlessly, or the method will
     * throw Exception.
     * @param range, must can be concat with this range seamlessly.
     * @return a combined range, eg: [95, 100] concat [99, 102] => [95, 102].
     */
    MatchRange concat(MatchRange range);
}
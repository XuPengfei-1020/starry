package automate.transition;

/**
 * A range of a set of characters that all can match one or more AtomOperand in a regular expression.
 * example:
 * [min, max] can match '.' in regular expression.
 * [97, 97] can match '.' in regular expression.
 *
 * @author flying
 */
public class DefaultMatchRange implements MatchRange {
    /** from and to **/
    private short from;
    private short to;

    /**
     * Constructor
     */
    public DefaultMatchRange(short from, short to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public short from() {
        return from;
    }

    @Override
    public short to() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DefaultMatchRange)) {
            return false;
        }

        DefaultMatchRange range = (DefaultMatchRange)obj;
        return range.to == this.to && range.from == this.from;
    }

    @Override
    public int hashCode() {
        return from * 100000000 + to * 10000;
    }

    @Override
    public String toString() {
        if (from == to) {
            return (char)from + "(" + from + ")";
        }

        return "[" + (char) from + "(" + from + ") - " + (char) to + "(" + to + ")]";
    }
}
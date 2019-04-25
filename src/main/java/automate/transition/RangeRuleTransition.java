package automate.transition;

/**
 * Transition with contains single or multiple {@link MatchRange}.
 * {@link #matchRanges} means ranges of character that can be accepted.
 * @todo make a cache pool to avoid create to much instance.
 * @author fling
 */
public class RangeRuleTransition implements Transition<RangeRuleTransition> {
    /** ranges ranges of character that can be accepted. **/
    private MatchRange[] matchRanges;

    /**
     * true means match will failed if gavin character in the any range of this's.
     */
    private boolean excludeMode = false;

    /**
     * Constructor
     * @param range range of character that can be accepted.
     */
    public RangeRuleTransition(MatchRange range) {
        if (range == null) {
            throw new RuntimeException("param: 'range' is null");
        }

        this.matchRanges = new MatchRange[1];
        matchRanges[0] = range;
    }

    /**
     * Constructor
     * @param range range range of character that can be accepted.
     * @param excludeMode true means match will failed if gavin character in the any range of this's.
     */
    public RangeRuleTransition(MatchRange range, boolean excludeMode) {
        this(range);
        this.excludeMode = excludeMode;
    }

    /**
     * Constructor
     * @param ranges ranges of character that can be accepted.
     */
    public RangeRuleTransition(MatchRange[] ranges) {
        if (ranges == null) {
            throw new RuntimeException("param: 'ranges' is null");
        }

        this.matchRanges = ranges;
    }

    /**
     * Constructor
     * @param ranges of character that can be accepted.
     * @param excludeMode true means match will failed if gavin character in the any range of this's.
     */
    public RangeRuleTransition(MatchRange[] ranges, boolean excludeMode) {
        this(ranges);
        this.excludeMode = excludeMode;
    }

    @Override
    public boolean match(short c) {
        for (MatchRange range : matchRanges) {
            if (range.from() <= c && range.to() >= c) {
                return !excludeMode;
            }
        }

        return excludeMode;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(excludeMode ? "[^" : "");

        for (MatchRange range: matchRanges) {
            result.append(range).append(",");
        }

        result.append(excludeMode ? "]" : "");
        return result.toString();
    }
}
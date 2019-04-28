package automate.transition;

import java.util.Collection;
import java.util.HashSet;

/**
 * Transition with contains single or multiple {@link MatchRange}.
 * {@link #matchRanges} means ranges of character that can be accepted.
 * @todo make a cache pool to avoid create to much instance.
 * @author fling
 */
public class RangeRuleTransition implements Transition {
    /** ranges ranges of character that can be accepted. **/
    private Collection<MatchRange> matchRanges = new HashSet<>();

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

        matchRanges.add(range);
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

        for (MatchRange range : ranges) {
            this.matchRanges.add(range);
        }
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
        return match(c, c);
    }

    @Override
    public boolean match(short from, short to) {
        for (MatchRange range : matchRanges) {
            if (range.from() >= from && range.to() <= to) {
                return !excludeMode;
            }
        }

        return excludeMode;
    }

    @Override
    public Collection<MatchRange> matchRanges() {
        return matchRanges;
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
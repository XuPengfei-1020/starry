package automate.transition;

import java.util.*;

/**
 * Transition with containsNFAState single or multiple {@link MatchRange}.
 * {@link #matchRanges} means ranges of character that can be accepted.
 * @todo make a cache pool to avoid create to much instance.
 * @author fling
 */
public class RangeRuleTransition implements Transition {
    /**
     * ranges of character that can be accepted or not which up to {@link #excludeMode}.
     */
    private ArrayList<MatchRange> matchRanges;

    /**
     * true means match will failed if gavin character in the any range of this's.
     */
    private boolean excludeMode;

    /**
     * cache toString
     */
    private final String toString;

    /**
     * Constructor
     * @param range range of character that can be accepted.
     */
    public RangeRuleTransition(MatchRange range) {
        this(range, false);
    }

    /**
     * Constructor
     * @param range range range of character that can be accepted.
     * @param excludeMode true means match will failed if gavin character in the any range of this's.
     */
    public RangeRuleTransition(MatchRange range, boolean excludeMode) {
        this(new MatchRange[] {range}, excludeMode);
    }

    /**
     * Constructor
     * @param ranges ranges of character that can be accepted.
     */
    public RangeRuleTransition(MatchRange[] ranges) {
        this(ranges, false);
    }

    /**
     * Constructor
     * @param ranges of character that can be accepted.
     * @param excludeMode true means match will failed if gavin character in the any range of this's.
     */
    public RangeRuleTransition(MatchRange[] ranges, boolean excludeMode) {
        if (ranges == null) {
            throw new RuntimeException("param: 'ranges' is null");
        }

        matchRanges = new ArrayList<>(ranges.length);
        Collections.addAll(matchRanges, ranges);
        this.excludeMode = excludeMode;
        toString = toString0();
        transferIncludeRange();
    }

    @Override
    public boolean match(short c) {
        return match(c, c);
    }

    @Override
    public boolean match(short from, short to) {
        for (MatchRange range : matchRanges) {
            if (range.from() <= from && range.to() >= to) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Collection<MatchRange> matchRanges() {
        return matchRanges;
    }

    @Override
    public String toString() {
        return toString;
    }

    private String toString0() {
        StringBuilder result = new StringBuilder();
        result.append(excludeMode ? "[^" : "");

        for (MatchRange range: matchRanges) {
            result.append(range).append(",");
        }

        result.append(excludeMode ? "]" : "");
        return result.toString();
    }

    /**
     * transfer gavin ranges to ranges of character can pass through.
     */
    private void transferIncludeRange() {
        Collections.sort(matchRanges, (MatchRange r1, MatchRange r2) -> r1.from() == r2.from() ?
            Short.compare(r1.to(), r2.to()) : Short.compare(r1.from(), r2.from()));

        if (!excludeMode) {
            return;
        }

        ArrayList<short[]> nonintersecting = new ArrayList<short[]>() {{add(matchRanges.get(0).range());}};

        for (MatchRange range : matchRanges) {
            short[] last = nonintersecting.get(nonintersecting.size() - 1);

            if (range.from() <= last[1] && range.to() > last[1]) {
                // intersecting, range is not a subset of current, extend current.
                last[1] = range.to();
                continue;
            }

            // un intersecting
            nonintersecting.add(range.range());
        }

        short left = Short.MIN_VALUE;
        matchRanges.clear();

        for (short[] range : nonintersecting) {
            if (left < range[0]) {
                matchRanges.add(new DefaultMatchRange(left, (short) (range[0] - 1)));
            }

            if (range[1] == Short.MAX_VALUE) {
                break;
            }

            left = (short) (range[1] + 1) ;
        }

        if (nonintersecting.size() != 0 && nonintersecting.get(nonintersecting.size() - 1)[1] != Short.MAX_VALUE) {
            matchRanges.add(new DefaultMatchRange((short) (nonintersecting.get(nonintersecting.size() - 1)[1] + 1),
                Short.MAX_VALUE));
        }
    }
}
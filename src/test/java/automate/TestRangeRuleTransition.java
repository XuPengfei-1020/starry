package automate;

import automate.transition.DefaultMatchRange;
import automate.transition.MatchRange;
import automate.transition.RangeRuleTransition;
import automate.transition.Transition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedHashMap;

public class TestRangeRuleTransition {
    // todo
    public void testMatch() {
        LinkedHashMap<short[][], short[][]> testCase = new LinkedHashMap<>();
        testCase.put(new short[][] {{97, 98}},
            new short[][] {{97, 98}});

        testCase.put(new short[][] {{97, 102}, {109, 130}},
            new short[][] {{97, 98}, {109, 120}, {110, 130}});

        testCase.put(new short[][] {{97, 102}, {109, 130}, {125, 155}},
            new short[][] {{97, 98}, {109, 120}, {125, 130}, {}});

        testCase.forEach((short[][] ranges, short[][] expect) -> {
        });
    }

    @Test
    public void testRangeRuleTransition() {
        LinkedHashMap<short[][], short[][]> testCase = new LinkedHashMap<>();
        testCase.put(new short[][] {{97, 98}},
            new short[][] {{97, 98}});
        testCase.put(new short[][] {{97, 98}, {97, 98}},
            new short[][] {{97, 98}, {97, 98}});

        testCase.forEach((short[][] ranges, short[][] expect) -> {
            Assert.assertTrue(testIncludeRange(buildTransition(ranges, false), expect));
        });

        testCase.clear();
        testCase.put(new short[][] {{97, 98}},
            new short[][] {{Short.MIN_VALUE, 96}, {99, Short.MAX_VALUE}});
        testCase.put(new short[][] {{97, 100}, {97, 98}},
            new short[][] {{Short.MIN_VALUE, 96}, {101, Short.MAX_VALUE}});

        testCase.put(new short[][] {{97, 100}, {98, 100},{97, 98}},
            new short[][] {{Short.MIN_VALUE, 96}, {101, Short.MAX_VALUE}});

        testCase.put(new short[][] {{Short.MIN_VALUE + 1, 100}, {100, Short.MAX_VALUE - 1}},
            new short[][] {{Short.MIN_VALUE, Short.MIN_VALUE}, {Short.MAX_VALUE, Short.MAX_VALUE}});

        testCase.put(new short[][] {{100, 101}, {101, 103}, {109, 110}, {120, 130}, {125, 130}, {133, 140},
                {139, 140}, {140, 155}},
            new short[][] {{Short.MIN_VALUE, 99}, {104, 108}, {111, 119}, {131, 132}, {156, Short.MAX_VALUE}});

        testCase.forEach((short[][] ranges, short[][] expect) -> {
            Assert.assertTrue(testIncludeRange(buildTransition(ranges, true), expect));
        });
    }

    private Transition buildTransition(short[][] ranges, boolean excludeMode) {
        DefaultMatchRange[] matchRanges = new DefaultMatchRange[ranges.length];

        for (int i = 0; i < ranges.length; i++) {
            matchRanges[i] = new DefaultMatchRange(ranges[i][0], ranges[i][1]);
        }

        return new RangeRuleTransition(matchRanges, excludeMode);
    }

    private boolean testIncludeRange(Transition transition, short[][] expect) {
        Collection<MatchRange> matchRanges = transition.matchRanges();
        short[][] ranges = new short[matchRanges.size()][];
        int i = 0;

        for (MatchRange range : matchRanges) {
            ranges[i++] = range.range();
        }

        return TestUtil.compareRanges(ranges, expect);
    }
}
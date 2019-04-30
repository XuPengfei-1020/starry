package automate;

import automate.DFA.DFA;
import automate.NFA.NFA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import reg.AbstractSyntaxTreeLoader;
import reg.CharacterReader;
import reg.factor.FactorReader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * test DFA
 */
public class TestDFA {
    private static DFA dfa;

    @BeforeClass
    public static void buildDFA() throws Exception {
        FactorReader factorReader = new FactorReader(new CharacterReader("abc"));
        AbstractSyntaxTreeLoader loader = new AbstractSyntaxTreeLoader();

        while (factorReader.hasNext()) {
            loader.receive(factorReader.next());
        }

        dfa = new DFA(new NFA(loader.rootOfAbstractSyntaxTree()));
    }

    @Test
    public void testHashRanges() throws Exception {
        Method m = dfa.getClass().getDeclaredMethod("hashRanges", LinkedList.class);
        m.setAccessible(true);
        ArrayList<HashRangesCrossReference> crossReferences = new ArrayList<>();
        crossReferences.add(new HashRangesCrossReference(
            new short[][] {{Short.MIN_VALUE, Short.MAX_VALUE}},
            new short[][] {{Short.MIN_VALUE, Short.MAX_VALUE}}));

        crossReferences.add(new HashRangesCrossReference(
            new short[][] {{Short.MIN_VALUE, 100}, { 101, 101}, {102,190}, {200,Short.MAX_VALUE}},
            new short[][] {{Short.MIN_VALUE, 100}, { 101, 101}, {102,190}, {200,Short.MAX_VALUE}}));

        crossReferences.add(new HashRangesCrossReference(
            new short[][] {{Short.MIN_VALUE, 100,}, {96, 97}, {97, 97}, {98, 99}, {100, 150}, {200, 300}},
            new short[][] {{Short.MIN_VALUE, 95}, {96, 96}, {97, 97}, {98, 99}, {100, 100}, {101, 150}, {200, 300}}));

        crossReferences.add(new HashRangesCrossReference(
            new short[][] {{Short.MIN_VALUE, 100}, { 98, 101}, {105, Short.MAX_VALUE}},
            new short[][] {{Short.MIN_VALUE, 97}, {98, 100}, {101, 101}, {105, Short.MAX_VALUE}}));

        crossReferences.add(new HashRangesCrossReference(
            new short[][] {{87, 92}, {87, 95},{99, 100}, {96, 97}, {97, 97}, {98, 99}, {100, 150}, {125, 130}, {149, 170}},
            new short[][] {{87, 92}, {93, 95}, {96, 96}, {97, 97}, {98, 98}, {99, 99}, {100, 100}, {101, 124}, {125, 130}, {131, 148}, {149, 150}, {151, 170}}));

        for (HashRangesCrossReference crossReference : crossReferences) {
            Assert.assertTrue(crossReference.valid((ArrayList<short[]>) m.invoke(dfa, crossReference.rawRanges)));
        }
    }


    private static class HashRangesCrossReference {
        public LinkedList<short[]> rawRanges = new LinkedList<>();
        public ArrayList<short[]> expect = new ArrayList<>();

        public HashRangesCrossReference(short[][] rawRanges, short[][] expect) {
            for (short[] rawRange : rawRanges) {
                this.rawRanges.add(rawRange);
            }

            for (short[] rawRange : expect) {
                this.expect.add(rawRange);
            }

            this.expect.sort((short[] r1, short[] r2) ->
                r1[0] == r2[0] ? Short.compare(r1[1], r2[1]) : Short.compare(r1[0], r2[0]));
        }

        public boolean valid(ArrayList<short[]> result) {
            return TestUtil.compareRanges(result, expect);
        }
    }
}
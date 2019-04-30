package automate;

import automate.DFA.DFA;
import automate.NFA.NFA;
import org.junit.Test;
import reg.AbstractSyntaxTreeLoader;
import reg.CharacterReader;
import reg.factor.Factor;
import reg.factor.FactorReader;
import reg.factor.operand.Operand;

import java.util.HashMap;
import java.util.Map;

public class TestConvertNFA_DFA {
    @Test
    public void test() throws Exception {
        String expression = "a|b*c?d+(ef)+(g|h)*[^abc][a-z]{0,2}fa{0,}b{,3}h{2,5}";
        // expression = "a|b*c?d+";
        CharacterReader reader = new CharacterReader(expression);
        FactorReader factorReader = new FactorReader(reader);
        AbstractSyntaxTreeLoader loader = new AbstractSyntaxTreeLoader();

        while (factorReader.hasNext()) {
            Factor factor = factorReader.next();
            loader.receive(factor);
        }

        //In fact, loader is a abstract syntax tree.
        Operand operand = loader.rootOfAbstractSyntaxTree();
        System.out.println(operand.expression());
        NFA nfa = new NFA(operand);
        System.out.println(nfa);
        // DrawAutoMate.draw(nfa.start());
        testReg();
    }

    private void testReg() throws Exception {
        HashMap<String, String[]> regAndText = new HashMap<>();
        // single
        regAndText.put("abc", new String[]{"abc"});
        regAndText.put("a",new String[]{"a"});
        // group
        regAndText.put("[abc]", new String[]{"a", "b", "c"});
        regAndText.put("[^abc]", new String[]{"d", "e", "f"});

        // +
        regAndText.put("a+b+c+",new String[] {"aaaabbbccc", "abc", "abbc", "aabc", "abcc"});
        regAndText.put("a+", new String[] {"a" ,"aa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"});
        regAndText.put("[abc]+", new String[] {"a", "ab", "ac", "bc", "abcbcbcaabcacb"});
        regAndText.put("[^abc]+", new String[] {"e", "f", "g", "wqertioutrojhioiertjuoigj"});

        // ?
        regAndText.put("a?",new String[] {"a", ""});
        regAndText.put("a?b?c?", new String[] {"abc", "bc", "ac", "b", "c", ""});

        regAndText.put("[abc]?", new String[] {"a", "b", "c", ""});
        regAndText.put("[^abc]?", new String[] {"e", "d", "f", ""});

        // *
        regAndText.put("a*b*c*", new String[] {"a", "b", "c", "", "aaa", "bb", "cc", "abc", "aabbcc", "aacccc", "bbcc", "cccccccccccccc"});
        regAndText.put("a*",new String[] {"", "a", "aaaaaaaaaaaaaa"});
        regAndText.put("[abc]*", new String[] {"a", "b", "c", "", "abcbcaabc", "cbcbaa", "bccccbbbba"});
        regAndText.put("[^abc]*", new String[] {"d", "e", "f", "", "edfedfe", "dfefef", "ewrqwerqwerqwrsf"});

        // |
        regAndText.put("a|b", new String[] {"a", "b"});
        regAndText.put("a|b*",new String[] {"", "a", "b", "bbbbb"});
        regAndText.put("a|[abc]", new String[] {"a", "b", "c"});
        regAndText.put("a|[^abc]", new String[] {"a", "d", "e", "f"});
        regAndText.put("a|b|c|d", new String[] {"a", "b", "c", "d"});
        regAndText.put("a|b|(de)|d", new String[] {"a", "b", "de", "d"});

        // connect
        regAndText.put("ab", new String[] {"ab"});
        regAndText.put("a(ac)", new String[] {"aac"});
        regAndText.put("ab*", new String[] {"a", "ab", "abbbbbbbbbb"});
        regAndText.put("a[abc]", new String[] {"aa", "ab", "ac"});
        regAndText.put("a[^abc]", new String[] {"ae", "ar", "ay"});
        regAndText.put("ab(de)d", new String[] {"abded"});

        // closure
        regAndText.put("a|(bc)", new String[] {"a", "bc"});
        regAndText.put("a|([^abc])", new String[] {"a", "e", "f"});
        regAndText.put("a|(efc)*", new String[] {"a", "efc", "efcefc"});
        regAndText.put("a|(efc)*d", new String[] {"ad", "efcd", "efcefcd", "d"});

        for (Map.Entry<String, String[]> entry : regAndText.entrySet()) {
            matchReg(entry.getKey(), entry.getValue(), true);
        }

        // test incorrect
        regAndText.clear();
        // single
        regAndText.put("a",new String[]{"", "b", "e", "f"});
        regAndText.put("abc", new String[]{"abcd", "a", "c", "b", "wr"});
        // group
        regAndText.put("[abc]", new String[]{"d", "e", "f"});
        regAndText.put("[^abc]", new String[]{"a", "b", "c", "ab", "ac", "bc", ""});

        // +
        regAndText.put("a+b+c+",new String[] {"a", "b", "c", "ab", "ac", "bbb", "aaa", "ccc", "e", "d", "f"});
        regAndText.put("a+", new String[] {"", "d" ,"e", "ffffffffff"});
        regAndText.put("[abc]+", new String[] {"d", "ad", "ae", "h", "aabcabace"});
        regAndText.put("[^abc]+", new String[] {"a", "b", "c", "wqertioutrojhioiertjuoaigj"});

        // ?
        regAndText.put("a?",new String[] {"aaac", "e"});
        regAndText.put("a?b?c?", new String[] {"abcc", "cb", "ca", "cc", "aa", "bb", "acc"});

        regAndText.put("[abc]?", new String[] {"d", "e", "f", "aa", "bb", "cc"});
        regAndText.put("[^abc]?", new String[] {"a", "b", "c", "aa", "bb", "cc"});

        // *
        regAndText.put("a*b*c*", new String[] {"w", "e", "r", "cba", "ca", "ba", "bca", "aabbcca", "aacccca", "bbcca", "cccccccccccccca"});
        regAndText.put("a*",new String[] {"b", "aaaaaaaaaaaaaab"});
        regAndText.put("[abc]*", new String[] {"abce", "be", "ce", "eabcbcaabc", "cbecbaa", "bcceccbbbba"});
        regAndText.put("[^abc]*", new String[] {"a", "b", "c", "edfaedfe", "dfebfef", "ewrcqwerqwerqwrsf"});

        // |
        regAndText.put("a|b", new String[] {"c", "e"});
        regAndText.put("a|b*",new String[] {"e", "ab", "bbbbba"});
        regAndText.put("a|[abc]", new String[] {"ab", "bc", "e", "f"});
        regAndText.put("a|[^abc]", new String[] {"b", "c", "ef", "dd"});
        regAndText.put("a|b|c|d", new String[] {"e", "f", "g", "h", ""});
        regAndText.put("a|b|(de)|d", new String[] {"f", "g", "ded", "dea", "e"});

        // connect
        regAndText.put("ab", new String[] {"abc", "", "a", "b"});
        regAndText.put("a(ac)", new String[] {"cc", "a", "ac", "c", "aacd"});
        regAndText.put("ab*", new String[] {"b", "bb", "bbbbbbbbbb", "", "abbba"});
        regAndText.put("a[abc]", new String[] {"aaa", "aba", "aca"});
        regAndText.put("a[^abc]", new String[] {"aea", "ara", "aya", ""});
        regAndText.put("ab(de)d", new String[] {"abdeda", "abd", "de"});

        // closure
        regAndText.put("a|(bc)", new String[] {"ac", "abc"});
        regAndText.put("a|([^abc])", new String[] {"aa", "b", "c"});
        regAndText.put("a|(efc)*", new String[] {"aef", "aefcc", "efca"});
        regAndText.put("a|(efc)*d", new String[] {"aed", "aefd", "aefcd"});

        for (Map.Entry<String, String[]> entry : regAndText.entrySet()) {
            matchReg(entry.getKey(), entry.getValue(), false);
        }
    }

    private void matchReg(String reg, String[] text, boolean expect) throws Exception {
        NFA nfa = buildNFA(reg);
        DFA dfa = new DFA(nfa);

        for (String t : text) {
            if(nfa.match(t) != expect) {
                throw new RuntimeException("NFA test failed: reg:" + reg + ", text:'" + t + "', expect:" + expect);
            }

            if(dfa.match(t) != expect) {
                throw new RuntimeException("DFA test failed: reg:" + reg + ", text:'" + t + "', expect:" + expect);
            }
        }
    }

    private NFA buildNFA(String reg) throws Exception {
        FactorReader factorReader = new FactorReader(new CharacterReader(reg));
        AbstractSyntaxTreeLoader loader = new AbstractSyntaxTreeLoader();

        while (factorReader.hasNext()) {
            loader.receive(factorReader.next());
        }

        return new NFA(loader.rootOfAbstractSyntaxTree());
    }
}
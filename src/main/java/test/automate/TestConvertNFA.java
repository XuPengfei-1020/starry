package test.automate;

import automate.NFA.NFA;
import automate.ui.DrawAutoMate;
import reg.AbstractSyntaxTreeLoader;
import reg.CharacterReader;
import reg.factor.Factor;
import reg.factor.FactorReader;
import reg.factor.operand.Operand;

public class TestConvertNFA {
    public static void main(String[] args) throws Exception {
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
        DrawAutoMate.draw(nfa.start());
    }
}
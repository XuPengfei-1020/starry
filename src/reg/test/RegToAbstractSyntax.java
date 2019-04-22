package reg.test;

import reg.AbstractSyntaxTreeLoader;
import reg.CharacterReader;
import reg.factor.Factor;
import reg.factor.FactorReader;

public class RegToAbstractSyntax {
    public static void main(String[] args) throws Exception {
        String expression = "ab{5,}a(ab\\n)*|b{1, 10}b{,10}[a-z][^a-z][2435][^243]a+(ab)|c";
        // expression = "a|b*";
        CharacterReader reader = new CharacterReader(expression);
        FactorReader factorReader = new FactorReader(reader);
        AbstractSyntaxTreeLoader loader = new AbstractSyntaxTreeLoader();

        while (factorReader.hasNext()) {
            Factor factor = factorReader.next();
            System.out.print(factor.expression());
            loader.receive(factor);
        }

        //In fact, loader is a abstract syntax tree.
        System.out.println();
        System.out.println(loader.rootOfAbstractSyntaxTree().expression());
    }
}
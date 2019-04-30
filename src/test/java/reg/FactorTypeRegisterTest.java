package reg;

import org.junit.Assert;
import org.junit.Test;
import reg.factor.FactorTypeRegister;

public class FactorTypeRegisterTest {
    @Test
    public void test() {
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_CLOSURE_MASK, FactorTypeRegister.CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_CLOSURE_MASK, FactorTypeRegister.CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.OPERAND_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.OPERATOR_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.OPERATOR_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_BRACE, FactorTypeRegister.LEFT_CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_CLOSURE_MASK, FactorTypeRegister.LEFT_CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_CLOSURE_MASK, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_BRACKET, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_PARENTHESIS, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_BRACE, FactorTypeRegister.BRACE_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.BINARY_OPERATOR_MASK, FactorTypeRegister.OPERATOR_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.BINARY_OPERATOR_MASK));
        Assert.assertTrue(FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.UNARY_OPERATOR_MASK));

        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.GROUP_OPERAND, FactorTypeRegister.CHARACTER_ATOM));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.OPERAND_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.OR));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.AT_LAST_ONCE, FactorTypeRegister.OPERAND_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.AT_LAST_ONCE));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.UNARY_OPERATOR_MASK));

        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.CLOSURE_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.LEFT_CLOSURE_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.AT_LAST_ONCE, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.OPERAND_MASK));
        Assert.assertTrue(!FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.RIGHT_BRACE));
    }
}

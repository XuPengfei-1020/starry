package test.reg;

import reg.factor.FactorTypeRegister;

public class FactorTypeRegisterTest {
    public static void main(String[] args) {
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_CLOSURE_MASK, FactorTypeRegister.CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_CLOSURE_MASK, FactorTypeRegister.CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.OPERAND_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.OPERATOR_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.OPERATOR_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_BRACE, FactorTypeRegister.LEFT_CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_CLOSURE_MASK, FactorTypeRegister.LEFT_CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_CLOSURE_MASK, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_BRACKET, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.RIGHT_PARENTHESIS, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.LEFT_BRACE, FactorTypeRegister.BRACE_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.BINARY_OPERATOR_MASK, FactorTypeRegister.OPERATOR_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.BINARY_OPERATOR_MASK));
        check(FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.UNARY_OPERATOR_MASK));

        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.GROUP_OPERAND, FactorTypeRegister.CHARACTER_ATOM));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.OPERAND_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.OR));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.AT_LAST_ONCE, FactorTypeRegister.OPERAND_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.AT_LAST_ONCE));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.UNARY_OPERATOR_MASK));

        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.OR, FactorTypeRegister.CLOSURE_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.CONNECT, FactorTypeRegister.LEFT_CLOSURE_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.AT_LAST_ONCE, FactorTypeRegister.RIGHT_CLOSURE_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.STAR, FactorTypeRegister.OPERAND_MASK));
        check(!FactorTypeRegister.instanceOf(FactorTypeRegister.CHARACTER_ATOM, FactorTypeRegister.RIGHT_BRACE));

    }

    public static void check(boolean t) {
        if (!t) {
            throw new AssertionError("");
        }
    }
}

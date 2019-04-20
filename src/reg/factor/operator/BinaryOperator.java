package reg.factor.operator;

import reg.factor.FactorTypeRegister;

/**
 * binary operator.
 *
 * @author flying
 */
public abstract class BinaryOperator implements Operator {
    /**
     * Connect operator， singleton
     */
    public static final Connect CONNECT = new Connect();

    /**
     * Or operator， singleton
     */
    public static final Or OR = new Or();

    @Override
    public boolean unary() {
        return false;
    }

    /**
     * Connect operand, ab is a connect b, connect is not visible.
     */
    public static class Connect extends BinaryOperator {
        /**
         * private constructor
         */
        private Connect() {
        }

        @Override
        public String expression() {
            return "。";
        }

        @Override
        public short type() {
            return FactorTypeRegister.CONNECT;
        }

        @Override
        public int precedence() {
            return 15;
        }
    }

    /**
     * or operator |
     */
    public static class Or extends BinaryOperator {
        /**
         * private constructor
         */
        private Or() {
        }

        @Override
        public String expression() {
            return "|";
        }

        @Override
        public short type() {
            return FactorTypeRegister.OR;
        }

        @Override
        public int precedence() {
            return 20;
        }
    }
}
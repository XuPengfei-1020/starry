package reg.factor.operator;

import reg.factor.FactorTypeRegister;

public abstract class UnaryOperator implements Operator {
    /**
     *  start operator， singleton
     */
    public static final Star STAR = new Star();

    /**
     * AtLastOnce operator, singleton
     */
    public static final AtLastOnce AT_LAST_ONCE = new AtLastOnce();

    /**
     * AtMostOnce operator，singleton
     */
    public static final AtMostOnce AT_MOST_ONCE = new AtMostOnce();

    @Override
    public boolean unary() {
        return true;
    }

    @Override
    public byte precedence() {
        return 10;
    }

    /**
     * Number of occurrence. {n, m}
     */
    public static class NumOfOccurrence extends UnaryOperator {
        /**
         * min
         */
        private int n;

        /**
         * max
         */
        private int m;

        /**
         * Constructor
         * @param n min
         * @param m max
         */
        public NumOfOccurrence(int n, int m) {
            this.n = n;
            this.m = m;

            if (n > m) {
                throw new RuntimeException("min: " + n + " is grate than max: " + m);
            }

            if (n < 0) {
                throw new RuntimeException("times setting can not lower than zero");
            }
        }

        /**
         * Constructor
         * @param n min, m is MAX_VALUE
         */
        public NumOfOccurrence(int n) {
            this(n, Integer.MAX_VALUE);
        }

        @Override
        public String expression() {
            return "{" + n + ", " + (m == Integer.MAX_VALUE ? "" : m ) + "}";
        }

        @Override
        public short type() {
            return FactorTypeRegister.NUM_OF_OCCURRENCE;
        }
    }

    /**
     * start operator, *
     */
    public static class Star extends UnaryOperator {
        /**
         * private constructor
         */
        private Star() {
        }

        @Override
        public String expression() {
            return "*";
        }

        @Override
        public short type() {
            return FactorTypeRegister.STAR;
        }
    }

    /**
     * at last once, +
     */
    public static class AtLastOnce extends UnaryOperator {
        /**
         * private constructor
         */
        private AtLastOnce() {
        }

        @Override
        public String expression() {
            return "+";
        }

        @Override
        public short type() {
            return FactorTypeRegister.AT_LAST_ONCE;
        }
    }

    /**
     * at most once,
     */
    public static class AtMostOnce extends UnaryOperator {
        /**
         * private constructor
         */
        private AtMostOnce() {
        }

        @Override
        public String expression() {
            return "?";
        }

        @Override
        public short type() {
            return FactorTypeRegister.AT_MOST_ONCE;
        }
    }
}
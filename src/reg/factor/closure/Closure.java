package reg.factor.closure;

import reg.factor.Factor;
import reg.factor.FactorTypeRegister;

/**
 * Closure, (, ), [, ], {, }
 */
public abstract class Closure implements Factor {
    /**
     * follows is singleton instance.
     */
    public static final Parenthesis LEFT_PARENTHESIS = new Parenthesis(true);
    public static final Parenthesis RIGHT_PARENTHESIS = new Parenthesis(false);
    public static final Brace LEFT_BRACE = new Brace(true);
    public static final Brace RIGHT_BRACE = new Brace(false);
    public static final Bracket LEFT_BRACKET = new Bracket(true);
    public static final Bracket RIGHT_BRACKET = new Bracket(false);
    public static final AntiBracket ANTI_BRACKET = new AntiBracket();

    /**
     * left or right
     */
    private boolean left;

    /**
     * corresponding symbol.
     * @return
     */
    public abstract String corresponding();

    /**
     * check if gavin corresponding is valid.
     * @return
     */
    public boolean match(Closure corresponding) {
        return corresponding != null && corresponding.getClass() == this.getClass() && corresponding.left() != this.left();
    }

    /**
     * Constructor
     */
    public Closure(boolean left) {
        this.left = left;
    }

    public boolean left() {
        return left;
    }

    public boolean right() {
        return !left;
    }

    /**
     * ( or )
     */
    public static class Parenthesis extends Closure {
        public static String LEFT = "(";
        public static String RIGHT = ")";

        /**
         * Constructor
         */
        private Parenthesis(boolean left) {
            super(left);
        }

        @Override
        public String expression() {
            return left() ? LEFT : RIGHT;
        }

        @Override
        public int type() {
            return left() ?  FactorTypeRegister.LEFT_PARENTHESIS : FactorTypeRegister.RIGHT_PARENTHESIS;
        }

        @Override
        public String corresponding() {
            return !left() ? LEFT : RIGHT;
        }
    }

    /**
     * [ or ]
     */
    public static class Bracket extends Closure {
        public static String LEFT = "[";
        public static String RIGHT = "]";

        /**
         * Constructor
         */
        private Bracket(boolean left) {
            super(left);
        }

        @Override
        public String expression() {
            return left() ? LEFT : RIGHT;
        }

        @Override
        public int type() {
            return left() ?  FactorTypeRegister.LEFT_BRACKET : FactorTypeRegister.RIGHT_BRACKET;
        }

        @Override
        public String corresponding() {
            return !left() ? LEFT : RIGHT;
        }

        @Override
        public boolean match(Closure corresponding) {
            if (super.match(corresponding)) {
                return true;
            }

            return corresponding != null && !left() && corresponding.getClass() == AntiBracket.class;
        }
    }

    /**
     * [^
     */
    public static class AntiBracket extends Bracket {
        public static String LEFT = "[^";

        /**
         * Constructor
         */
        private AntiBracket() {
            super(true);
        }

        @Override
        public String expression() {
            return LEFT;
        }

        @Override
        public int type() {
            return left() ?  FactorTypeRegister.ANTI_BRACKET : FactorTypeRegister.RIGHT_BRACKET;
        }

        @Override
        public String corresponding() {
            return RIGHT;
        }

        @Override
        public boolean match(Closure corresponding) {
            return corresponding != null && corresponding.getClass() == Bracket.class && corresponding.left();
        }
    }

    /**
     * { or }
     */
    public static class Brace extends Closure {
        public static String LEFT = "{";
        public static String RIGHT = "}";

        /**
         * Constructor
         */
        private Brace(boolean left) {
            super(left);
        }

        @Override
        public String expression() {
            return left() ? LEFT : RIGHT;
        }

        @Override
        public int type() {
            return left() ?  FactorTypeRegister.LEFT_BRACE : FactorTypeRegister.RIGHT_BRACE;
        }

        @Override
        public String corresponding() {
            return !left() ? LEFT : RIGHT;
        }
    }
}
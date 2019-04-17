package reg.factor.closure;

import reg.factor.Factor;

/**
 * Closure, (, ), [, ], {, }
 */
public abstract class Closure implements Factor {
    /**
     * left or right
     */
    private boolean left;

    /**
     * corresponding symbol.
     * @return
     */
    public abstract String corresponding();

    @Override
    public boolean isClosure() {
        return true;
    }

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
        public Parenthesis(boolean left) {
            super(left);
        }

        @Override
        public String letter() {
            return left() ? LEFT : RIGHT;
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
        public Bracket(boolean left) {
            super(left);
        }

        @Override
        public String letter() {
            return left() ? LEFT : RIGHT;
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
        public static String RIGHT = "]";

        /**
         * Constructor
         */
        public AntiBracket() {
            super(true);
        }

        @Override
        public String letter() {
            return LEFT;
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
        public Brace(boolean left) {
            super(left);
        }

        @Override
        public String letter() {
            return left() ? LEFT : RIGHT;
        }

        @Override
        public String corresponding() {
            return !left() ? LEFT : RIGHT;
        }
    }
}

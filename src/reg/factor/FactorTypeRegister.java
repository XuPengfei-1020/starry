package reg.factor;

/**
 * Register type of each factor.
 *
 * @author flying
 */
public class FactorTypeRegister {
    /**
     * The follows is mask for OPERATOR, OPERAND and CLOSURE.
     */
    private static final int MASK = 0x8000; // 2 ^ 15
    private static final int TYPE = 0x7FFF; // 2 ^ 15 -1

    public static final int OPERATOR_MASK = 0x4000 | MASK; // 2 ^ 14 | MASK
    public static final int OPERAND_MASK = 0x2000 | MASK; // 2 ^ 13 | MASK
    public static final int CLOSURE_MASK = 0x1000 | MASK; // 2 ^ 12 | MASK

    /**
     * The follows is mask for UNARY_OPERATOR and BINARY_OPERATOR
     */
    public static final int UNARY_OPERATOR_MASK = OPERATOR_MASK | 0x0800; // OPERATOR_MASK | 2 ^ 11
    public static final int BINARY_OPERATOR_MASK = OPERATOR_MASK | 0x0400; // OPERATOR_MASK | 2 ^ 10

    /**
     * The follows is type for START, AT_LAST_ONCE, AT_MOST_ONCE, NUM_OF_OCCURRENCE.
     */
    public static final int STAR = UNARY_OPERATOR_MASK | 1 & TYPE;
    public static final int AT_LAST_ONCE = UNARY_OPERATOR_MASK | 2 & TYPE;
    public static final int AT_MOST_ONCE = UNARY_OPERATOR_MASK | 3 & TYPE;
    public static final int NUM_OF_OCCURRENCE = UNARY_OPERATOR_MASK | 4 & TYPE;

    /**
     * The follows is type for OR and CONNECT
     */
    public static final int OR = BINARY_OPERATOR_MASK | 1  & TYPE;
    public static final int CONNECT = BINARY_OPERATOR_MASK | 2 & TYPE;

    /**
     * The follows is type for CHARACTER_ATOM , COMBINED_OPERAND and GROUP_OPERAND
     */
    public static final int CHARACTER_ATOM = OPERAND_MASK | 1  & TYPE;
    public static final int COMBINED_OPERAND = OPERAND_MASK | 2 & TYPE;
    public static final int GROUP_OPERAND = OPERAND_MASK | 3 & TYPE;

    /**
     * The follows is mask for LEFT_CLOSURE and RIGHT_CLOSURE
     */
    public static final int LEFT_CLOSURE_MASK = CLOSURE_MASK | 0x0800; // CLOSURE_MASK | 2 ^ 11
    public static final int RIGHT_CLOSURE_MASK = CLOSURE_MASK | 0x0400; // CLOSURE_MASK | 2 ^ 10

    /**
     * The follows is mask for ANTI_BRACKET, BRACKET, BRACE, PARENTHESIS
     */
    public static final int ANTI_BRACKET_MASK = CLOSURE_MASK | 0x0200; //CLOSURE_MASK | 2 ^ 9
    public static final int BRACKET_MASK = CLOSURE_MASK | 0x0100; //CLOSURE_MASK | 2 ^ 8
    public static final int BRACE_MASK = CLOSURE_MASK | 0x0080; //CLOSURE_MASK | 2 ^ 7
    public static final int PARENTHESIS_MASK = CLOSURE_MASK | 0x0040; //CLOSURE_MASK | 2 ^ 6

    /**
     * The follows is type for ANTI_BRACKET, BRACKET, BRACE, PARENTHESIS, left or right.
     */
    public static final int ANTI_BRACKET = LEFT_CLOSURE_MASK | ANTI_BRACKET_MASK & TYPE;
    public static final int LEFT_BRACKET = LEFT_CLOSURE_MASK | BRACKET_MASK & TYPE;
    public static final int RIGHT_BRACKET = RIGHT_CLOSURE_MASK | BRACKET_MASK & TYPE;
    public static final int LEFT_BRACE = LEFT_CLOSURE_MASK | BRACE_MASK & TYPE;
    public static final int RIGHT_BRACE = RIGHT_CLOSURE_MASK | BRACE_MASK & TYPE;
    public static final int LEFT_PARENTHESIS = LEFT_CLOSURE_MASK | PARENTHESIS_MASK & TYPE;
    public static final int RIGHT_PARENTHESIS = RIGHT_CLOSURE_MASK | PARENTHESIS_MASK & TYPE;

    /**
     * @param a type of a gavin type.
     * @param b type of a gavin instance.
     * @return true a is a instance of b;
     */
    public static boolean instanceOf(int a, int b) {
        if (a == b) {
            return true;
        }

        return ((b | TYPE) == TYPE) ? (a == b) : (((a | MASK) & b) == b);
    }

    /**
     * @return true means {@param factor} is an operator.
     */
    public static boolean isOperator(Factor factor) {
        return instanceOf(factor.type(), OPERATOR_MASK);
    }

    /**
     * @return true means {@param factor} is an operand.
     */
    public static boolean isOperand(Factor factor) {
        return instanceOf(factor.type(), OPERAND_MASK);
    }

    /**
     * @return true means {@param factor} is a closure.
     */
    public static boolean isClosure(Factor factor) {
        return instanceOf(factor.type(), CLOSURE_MASK);
    }
}
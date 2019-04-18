package reg;

/**
 * Character, include some special costume character.
 *
 * @author flying
 */
public class Character {
    /**
     * end of expression
     */
    public static final byte EOF = -1;

    /**       closure，from -101 to -200  */
    public static final short CPOSURE_START = -101;
    /**
     * (
     */
    public static final short LEFT_PARENTHESIS = -102;

    /**
     * )
     */
    public static final short RIGHT_PARENTHESIS = -103;

    /**
     * (
     */
    public static final short LEFT_BRACKET = -104;

    /**
     * ]
     */
    public static final short RIGHT_BRACKET = -105;

    /**
     * [^
     */
    public static final short ANTI_GROUP_START = -106;

    /**
     * [
     */
    public static final short LEFT_BRACE = -107;

    /**
     * )
     */
    public static final short RIGHT_BRACE = -108;

    /**
     * end whit 200
     */
    public static final short CLOSURE_END = -200;

    /** operator, from -201 to -300*/
    public static final short OPERATOR_START = -201;

    /**
     * * star operator
     */
    public static final short STAR = -202;

    /**
     * ? operator
     */
    public static final short AT_MOST_ONCE = -203;

    /**
     * + plus operator
     */
    public static final short AT_LAST_ONCE = -204;

    /**
     * | or operator
     */
    public static final short OR = -205;

    /** operator, end with -300*/
    public static final short OPERATOR_END = -300;

    /** special operand character, from -301 to -400*/
    public static final short SPECIAL_CHAR_START = -301;

    /**
     * . dot
     */
    public static final short DOT = -302;

    /**
     * \s, un-blank
     */
    public static final short UN_BLANK_ESCAPE = -303;

    /**
     * \S, blank
     */
    public static final short BLANK_ESCAPE = -304;

    /**
     * \n new line
     */
    public static final short NEW_LINE_ESCAPE = -305;

    /**
     * \t table
     */
    public static final short TABLE_ESCAPE = -306;

    /**
     * \r Carriage return
     */
    public static final short R_ESCAPE = -307;

    /**
     * special operand character, end with -400
     */
    public static final short SPECIAL_CHAR_END = -400;

    /**
     * Convert character to expression
     */
    public static String convertToLetter(int character) {
        switch (character) {
            case NEW_LINE_ESCAPE:
                return "\\n";
            case R_ESCAPE:
                return "\\r";
            case TABLE_ESCAPE:
                return "\\t";
            case UN_BLANK_ESCAPE:
                return "\\s";
            case BLANK_ESCAPE:
                return "\\s";
            case '.' | '*' | '|' | '+' | '?' | '(' | ')' | '[' | ']' | '{' | '}' | '\\':
                return "\\" + ((char) character);
            default:
                return String.valueOf ((char) character);
        }
    }

    /**
     * 转换转义字符
     */
    public static int convertEscape(int c) {
        switch (c) {
            case 'n':
                return NEW_LINE_ESCAPE;
            case 'r':
                return R_ESCAPE;
            case 't':
                return TABLE_ESCAPE;
            case 's':
                return UN_BLANK_ESCAPE;
            case 'S':
                return BLANK_ESCAPE;
            case '.' | '*' | '|' | '+' | '?' | '(' | ')' | '[' | ']' | '{' | '}' | '\\':
                return c;
            default:
                throw new RuntimeException("invalid escape:\\" + c);
        }
    }

    /**
     * 转换转义字符
     */
    public static int specilize(int c) {
        switch (c) {
            case '.':
                return DOT;
            case '*':
                return STAR;
            case '|':
                return OR;
            case '(':
                return LEFT_PARENTHESIS;
            case ')':
                return RIGHT_PARENTHESIS;
            case '[':
                return LEFT_BRACKET;
            case ']':
                return RIGHT_BRACKET;
            case '{':
                return LEFT_BRACE;
            case '}':
                return RIGHT_BRACE;
            case '?':
                return AT_MOST_ONCE;
            case '+':
                return AT_LAST_ONCE;
        }

        return c;
    }
}
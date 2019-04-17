package reg;

/**
 * check out character one by one from regular expression.
 * will auto convert escape character to corresponding value .
 *
 * @author flying
 */
public class CharacterReader {
    /**
     * regular expression
     */
    private String expression;

    /**
     * current index of regular expression
     */
    private int index = 0;

    /**
     * Constructor
     */
    public CharacterReader(String expression) {
        if (expression == null || expression.length() == 0) {
            throw new RuntimeException("expression is empty");
        }

        this.expression = compress(expression);
    }

    /**
     * 是否还有下一个字符
     * @return
     */
    public boolean hasNext() {
        return index < (expression.length());
    }

    /**
     * 读取下一个字符，重复调用会导致指针后移。
     * @return 下一个字符。
     */
    public int next() {
        if (index >= expression.length()) {
            return Character.EOF;
        }

        int c = expression.charAt(index++);

        if (c == '\\') {
            if (index >= expression.length()) {
                throw new RuntimeException("不合法的结尾：\\");
            }

            if (c == 'u') {
                // 遇到一个 \ u 开头的 unicode
                // todo, 加上此功能
                throw new RuntimeException("暂时不支持 unicode");
            }

            // 转义字符,再读取一个.
            return Character.convertEscape(expression.charAt(index++));
        }

        if (c == '[' && index < expression.length() && expression.charAt(index) == '^') {
            index ++;
            return Character.ANTI_GROUP_START;
        }

        // 尝试对 c 进行 specialize
        return Character.specilize(c);
    }

    /**
     * 往后探索几个字符，并且指针不随着移动。
     * @param n n 大于0，并且包含当前字符
     * @return 首个字符是当前指针指向的字符
     */
    public int[] explore(int n) {
        if (index >= expression.length()) {
            return new int[] {};
        }

        // 记住原来的 index
        int oldIndex = index;
        int[] result = new int[n];

        while (n-- > 0) {
            result[result.length - n - 1] = next();
        }

        // 还原 index
        index = oldIndex;
        return result;
    }

    /**
     * remove blank letter from string.
     */
    private String compress(String s) {
        // may be need a array to record map of index of each  character in old String and new String.
        StringBuilder sbf = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (!java.lang.Character.isWhitespace(c)) {
                sbf.append(c);
            }
        }

        return sbf.toString();
    }
}
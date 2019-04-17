package reg.factor.operand;

/**
 * 使用 unicode 表示的基本单元。
 * 延后实现
 */
public class UnicodeAtom extends CharacterAtom {
    /**
     * unicode 字符串
     */
    private int value;

    /**
     * Constructor
     * @param unicode
     */
    public UnicodeAtom(String unicode) {
        // todo, convert unicode to int
        super(Integer.parseInt(unicode));
    }
}
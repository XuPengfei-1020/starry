package reg.factor.operand;

/**
 * 使用 unicode 表示的基本单元。
 * 延后实现
 */
public class UnicodeAtom extends CharacterAtom {
    /**
     * unicode
     */
    private String unicode;

    /**
     * Constructor
     * @param unicode
     */
    public UnicodeAtom(String unicode) {
        // todo, convert unicode to int
        super(Short.parseShort(unicode));
        this.unicode = unicode;
    }

    @Override
    public String expression() {
        return getUnicode();
    }

    public String getUnicode() {
        return unicode;
    }
}
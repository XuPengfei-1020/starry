package reg.factor.operand;

import reg.Character;
import reg.factor.FactorTypeRegister;

/**
 * Single character operand
 */
public class CharacterAtom implements Operand {
    /**
     * Value of character， maybe a special character. see {@link Character}
     */
    private final int c;

    /**
     * true means the character is a special character, such as 'S' 's' '.' ...
     */
    private final boolean speicial;

    /**
     * Constructor
     * @param c
     */
    public CharacterAtom(int c) {
        this.c = c;
        speicial = c < Character.SPECIAL_CHAR_START && c > Character.SPECIAL_CHAR_END;
    }

    @Override
    public String expression() {
        return Character.convertToLetter(c);
    }

    @Override
    public int type() {
        return FactorTypeRegister.CHARACTER_ATOM;
    }

    /**
     * is special character， in [\s, \S, .]?
     * @return true is mean the character is special.
     */
    public boolean special() {
        return speicial;
    }

    /**
     * @return value of character.
     */
    public int character() {
        return c;
    }
}
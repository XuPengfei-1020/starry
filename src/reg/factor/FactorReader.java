package reg.factor;

import reg.Character;
import reg.CharacterReader;
import reg.factor.closure.Closure;
import reg.factor.operand.CharacterAtom;
import reg.factor.operator.BinaryOperator;
import reg.factor.operator.UnaryOperator;

/**
 * Factor reader， read factor from character reader sequential. not thread safe.
 *
 * @author flying
 */
public class FactorReader {
    /**
     * must not be null.
     */
    private CharacterReader reader;

    /**
     * Constructor
     */
    public FactorReader(CharacterReader reader) {
        if (reader == null) {
            throw new NullPointerException("Character must be not null");
        }

        this.reader = reader;
    }

    public boolean hasNext() {
        return reader.hasNext();
    }

    /**
     * Return next factor, return null means has reached end of expression.
     */
    public Factor next() {
        if (!hasNext()) {
            return null;
        }

        int character = reader.next();

        if (character == Character.LEFT_PARENTHESIS || character == Character.RIGHT_PARENTHESIS) {
            return new Closure.Parenthesis(character == Character.LEFT_PARENTHESIS);
        }

        if (character == Character.LEFT_BRACKET || character == Character.RIGHT_BRACKET) {
            return new Closure.Bracket(character == Character.LEFT_BRACKET);
        }

        if (character == Character.LEFT_BRACE || character == Character.RIGHT_BRACE) {
            return new Closure.Brace(character == Character.LEFT_BRACE);
        }

        if (character == Character.ANTI_GROUP_START) {
            return new Closure.AntiBracket();
        }

        if (character == Character.AT_LAST_ONCE) {
            return UnaryOperator.AT_LAST_ONCE;
        }

        if (character == Character.AT_MOST_ONCE) {
            return UnaryOperator.AT_MOST_ONCE;
        }

        if (character == Character.OR) {
            return BinaryOperator.OR;
        }

        if (character == Character.STAR) {
            return UnaryOperator.STAR;
        }

        return new CharacterAtom(character);
    }
}
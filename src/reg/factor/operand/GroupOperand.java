package reg.factor.operand;

import reg.factor.FactorTypeRegister;
import reg.factor.closure.Closure;

/**
 * Group of character, [a - z], [0-9], [^abc]
 * Members of group can be a set of characters, or a range of a set of characters, but the tow format is conflicts.
 *
 * @author flying
 */
public class GroupOperand implements Operand {
    /**
     * Separator for range.
     */
    public static final char RANGE_SEPARATOR = '-';

    /** ture means format is [^...], false is [...]**/
    private boolean antiMode;

    /**
     * members of this group, a set of character.
     * conflicts witch {@link #from} {@link #to}
     */
    private CharacterAtom[] members;

    /**
     * Min limit for range, if this group is a range of a set of characters.
     * range is conflicts with {@link #members}.
     */
    private CharacterAtom from;

    /**
     * Max limit for range, if this group is a range of a set of characters.
     * range is conflicts with {@link #members}.
     */
    private CharacterAtom to;

    /**
     * Constructor
     * @param anti true means group is anti mode.
     * @param members members of this group, a set of character.
     */
    public GroupOperand(boolean anti, CharacterAtom[] members) {
        this.antiMode = anti;

        if (members == null || members.length ==0) {
            throw new RuntimeException("Members is empty!");
        }

        this.members = members;
    }

    /**
     * Constructor
     * @param anti true means group is anti mode.
     * @param from Min limit for range.
     * @param to Max limit for range.
     */
    public GroupOperand(boolean anti, CharacterAtom from, CharacterAtom to) {
        this.antiMode = anti;

        if (from == null || to == null) {
            throw new RuntimeException("The limit for range should be start of end with a certain character");
        }

        if (from.special() || to.special()) {
            throw new RuntimeException("The limit for range should be start or end with a common character");
        }

        if (from.character() >= to.character()) {
            throw new RuntimeException("Range is invalid in logical, min is:" +  from.character() + ", max is:" + to.character());
        }

        this.from = from;
        this.to = to;
    }

    @Override
    public String expression() {
        StringBuilder expression = new StringBuilder();
        expression.append(antiMode ? Closure.AntiBracket.LEFT : Closure.Bracket.LEFT);

        if (members != null) {
            for (CharacterAtom members : members) {
                expression.append(members.expression());
            }
        } else {
            expression.append(from.expression()).append(RANGE_SEPARATOR).append(to.expression());
        }

        expression.append(Closure.Bracket.RIGHT);
        return expression.toString();
    }

    @Override
    public short type() {
        return FactorTypeRegister.GROUP_OPERAND;
    }

    public boolean isAntiMode() {
        return antiMode;
    }

    public CharacterAtom[] getMembers() {
        return members;
    }

    public CharacterAtom getFrom() {
        return from;
    }

    public CharacterAtom getTo() {
        return to;
    }
}

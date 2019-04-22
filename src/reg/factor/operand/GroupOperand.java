package reg.factor.operand;

import reg.factor.FactorTypeRegister;
import reg.factor.closure.Closure;

/**
 * Group of character, [a - z], [0-9], [^abc]
 * Members of group can be a set of characters, or a range of a set of characters, but the tow format is conflicts.
 *
 * @author flying
 */
public abstract class GroupOperand implements Operand {
    /** ture means format is [^...], false is [...]**/
    protected boolean antiMode;

    /**
     * @return true means this group is anti mode.
     */
    public boolean isAntiMode() {
        return antiMode;
    }

    /**
     * Group witch members can
     */
    public static class PartitionGroup extends GroupOperand {
        /**
         * Separator for range.
         */
        public static final char RANGE_SEPARATOR = '-';

        /**
         * Min limit for range, if this group is a range of a set of characters.
         */
        private CharacterAtom from;

        /**
         * Max limit for range, if this group is a range of a set of characters.
         */
        private CharacterAtom to;

        /**
         * Constructor
         * @param from Min limit for range.
         * @param to Max limit for range.
         */
        public PartitionGroup(CharacterAtom from, CharacterAtom to, boolean antiMode) {
            this.antiMode = antiMode;

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
        public short type() {
            return FactorTypeRegister.RANGE_GROUP_OPERAND;
        }

        @Override
        public String expression() {
            StringBuilder expression = new StringBuilder();
            expression.append(antiMode ? Closure.AntiBracket.LEFT : Closure.Bracket.LEFT);
            expression.append(from.expression()).append(RANGE_SEPARATOR).append(to.expression());
            expression.append(Closure.Bracket.RIGHT);
            return expression.toString();
        }

        public short from() {
            return from.character();
        }

        public short to() {
            return to.character();
        }
    }

    /**
     * Group witch members are not presented by a single partition.
     */
    public static class HashGroup extends GroupOperand {
        /**
         * members of this group, a set of character.
         */
        private CharacterAtom[] members;

        /**
         * Constructor
         * @param members members of this group, a set of character.
         */
        public HashGroup(CharacterAtom[] members, boolean antiMode) {
            this.antiMode = antiMode;

            if (members == null || members.length ==0) {
                throw new RuntimeException("Members is empty!");
            }

            this.members = members;
        }

        @Override
        public short type() {
            return FactorTypeRegister.RANGE_GROUP_OPERAND;
        }

        @Override
        public String expression() {
            StringBuilder expression = new StringBuilder();
            expression.append(antiMode ? Closure.AntiBracket.LEFT : Closure.Bracket.LEFT);

            for (CharacterAtom members : members) {
                expression.append(members.expression());
            }

            expression.append(Closure.Bracket.RIGHT);
            return expression.toString();
        }

        public CharacterAtom[] getMembers() {
            return members;
        }
    }
}
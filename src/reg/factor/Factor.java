package reg.factor;

/**
 * 一个 factor 表示一个 reg 语法中的单元。 它可以是一个 operator，可以是一个 operand， 或者是一个 closure flag (大小括号，'('，')'，用于运算改变优先级，标记 subset)
 */
public interface Factor {
    /**
     * @return expression of this factor， '\n' is "\\n", '\t' is "\\t"。 or is | ， star is *, and so on.
     */
    String expression();

    /**
     * @return true is factor is operator
     */
    default boolean isOperator() {
        return false;
    }

    /**
     * @return true is factor is operand
     */
    default boolean isOperand() {
        return false;
    }

    /**
     * @return true is factor is closure， left or right。
     */
    default boolean isClosure() {
        return false;
    }
}
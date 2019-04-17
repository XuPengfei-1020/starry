package reg.factor.operand;

import reg.factor.Factor;

/**
 * Operand， maybe a character，such as 'a', 'b' or 'c'， or combined operand, (operand operator operand);
 *
 * @author flying
 */
public interface Operand extends Factor {
    @Override
    default boolean isOperand() {
        return true;
    }
}
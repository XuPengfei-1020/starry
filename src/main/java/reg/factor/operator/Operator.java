package reg.factor.operator;

import reg.factor.Factor;

/**
 * 一个操作运算符， 一个操作运算符可以是单目运算符` *, +, ? 或者 {n, m} 表示的 times`，也可以是双目运算符 `|（或）或。（连接符）`
 * @author flying
 */
public interface Operator extends Factor {
    /**
     * the operator is associative?
     */
    boolean unary();

    /**
     * @return precedence of this operator
     */
    byte precedence();
}
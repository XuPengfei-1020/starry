package reg.factor.operand;

import reg.factor.FactorTypeRegister;
import reg.factor.closure.Closure;
import reg.factor.operator.Operator;

/**
 * Combined operand, which consist of some others factor. eg: ( a | b * )
 *
 * @author flying
 */
public class CombinedOperand implements Operand {
    /**
     * Left closure, can be null if rightClosure is null.
     */
    private Closure leftClosure;

    /**
     * Right closure, can be null if leftClosure is null.
     */
    private Closure rightClosure;

    /**
     * leftClosure operand, must not be null.
     */
    private Operand leftOperand;

    /**
     * operator, can be null.
     */
    private Operator operator;

    /**
     * rightClosure operand, must be null when operator is null.
     */
    private Operand rightOperand;

    /**
     * Constructor eg format: (a | b)
     */
    public CombinedOperand(Closure leftClosure, Operand leftOperand, Operator operator, Operand rightOperand, Closure rightClosure) {
        this.leftClosure = leftClosure;
        this.rightClosure = rightClosure;
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
        valid();
    }

    /**
     * Constructor, eg format: (a*)
     */
    public CombinedOperand(Closure leftClosure, Operand leftOperand, Operator operator, Closure rightClosure) {
        this(leftClosure, leftOperand, operator, null, rightClosure);
    }

    /**
     * Constructor, eg format: (a)
     */
    public CombinedOperand(Closure leftClosure, Operand leftOperand, Closure rightClosure) {
        this(leftClosure, leftOperand, null, null, rightClosure);
    }

    /**
     * Constructor, eg format: a | b
     */
    public CombinedOperand(Operand leftOperand, Operator operator, Operand rightOperand) {
        this(null, leftOperand, operator, rightOperand, null);
    }

    @Override
    public String expression() {
        return (leftClosure == null ? "" : leftClosure.expression()) +
                (leftOperand == null ? "" : leftOperand.expression()) +
                (operator == null ? "" : operator.expression()) +
                (rightOperand == null ? "" : rightOperand.expression()) +
                (rightClosure == null ? "" : rightClosure.expression());
    }

    @Override
    public int type() {
        return FactorTypeRegister.COMBINED_OPERAND;
    }

    /**
     * Constructor, eg format: a*, a+
     */
    public CombinedOperand(Operand leftOperand, Operator operator) {
        this(leftOperand, operator, null);
    }

    public Closure getLeftClosure() {
        return leftClosure;
    }

    public Closure getRightClosure() {
        return rightClosure;
    }

    public Operand getLeftOperand() {
        return leftOperand;
    }

    public Operator getOperator() {
        return operator;
    }

    public Operand getRightOperand() {
        return rightOperand;
    }

    /**
     * check if combine is valid.
     */
    private void valid() {
        if ((this.leftClosure == null) != (rightClosure == null)) {
            throw new RuntimeException("Left closure or rightClosure is not paired.");
        }

        if (leftOperand == null) {
            throw new RuntimeException("Inner operand (left operand) is null.");
        }

        if (rightOperand != null) {
            if (operator == null) {
                throw new RuntimeException("RightOperand can not connect with leftOperand without operator");
            }

            if (operator.unary()) {
                throw new RuntimeException("RightOperand can not connect with leftOperand in unary operator.");
            }
        }

        if (rightOperand == null && operator != null && !operator.unary()) {
            throw new RuntimeException("Miss rightOperand because operator is binary operator.");
        }
    }
}
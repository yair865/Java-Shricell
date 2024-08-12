package enginePackage.implementation.expressions;

import enginePackage.interfaces.Expression;

public class Sum extends BinaryExpression {

    public Sum(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "+";
    }

    @Override
    protected double evaluate(double e1, double e2) {
        return e1 + e2;
    }

}

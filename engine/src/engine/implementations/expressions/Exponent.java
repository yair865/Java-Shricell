package engine.implementations.expressions;

import engine.interfaces.Expression;

public class Exponent extends BinaryExpression {

    public Exponent(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "^";
    }

    @Override
    protected double evaluate(double e1, double e2) {
        return Math.pow(e1, e2);
    }

}
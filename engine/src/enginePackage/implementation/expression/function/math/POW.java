package enginePackage.implementation.expression.function.math;

import enginePackage.implementation.expression.function.general.BinaryExpression;
import enginePackage.interfaces.Expression;

public class POW extends BinaryExpression<Double> {

    public POW(Expression<Double> expression1, Expression<Double> expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Double evaluate(Double e1, Double e2) {
        return Math.pow(e1, e2);
    }

}
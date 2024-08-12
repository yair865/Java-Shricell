package enginePackage.implementation.expression.function.math;

import enginePackage.implementation.expression.function.general.BinaryExpression;
import enginePackage.interfaces.Expression;

public class PLUS extends BinaryExpression<Double> {

    public PLUS(Expression<Double> expression1, Expression<Double> expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Double evaluate(Double e1, Double e2) {
        return e1 + e2; // Directly add the two Double values
    }
}

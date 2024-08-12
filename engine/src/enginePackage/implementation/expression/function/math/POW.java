package enginePackage.implementation.expression.function.math;

import enginePackage.implementation.expression.function.general.BinaryExpression;
import enginePackage.interfaces.Expression;
import enginePackage.implementation.expression.type.Number;

public class POW extends BinaryExpression<Number> {

    public POW(Expression<Number> expression1, Expression<Number> expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Number evaluate(Number e1, Number e2) {
        return new Number(Math.pow(e1.evaluate(), e2.evaluate()));
    }
}
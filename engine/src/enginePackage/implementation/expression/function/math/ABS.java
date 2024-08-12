package enginePackage.implementation.expression.function.math;

import enginePackage.implementation.expression.function.general.BinaryExpression;
import enginePackage.implementation.expression.function.general.UnaryExpression;
import enginePackage.interfaces.Expression;
import enginePackage.implementation.expression.type.Number;

public class ABS extends UnaryExpression<Number> {

    public ABS(Expression<Number> expression){
        super(expression);
    }

    @Override
    protected Number evaluate(Number num) {
        return new Number(Math.abs(num.evaluate()));
    }
}

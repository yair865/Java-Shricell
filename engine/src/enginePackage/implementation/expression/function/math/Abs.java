package enginePackage.implementation.expression.function.math;

import enginePackage.api.EffectiveValue;
import enginePackage.implementation.expression.type.UnaryExpression;
import enginePackage.api.Expression;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    public EffectiveValue evaluate(Expression expression) {

        EffectiveValue expressionValue = expression.evaluate();

        // do some checking... error handling...

        double result = Math.abs(expressionValue.extractValueWithExpectation(Double.class));

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

}


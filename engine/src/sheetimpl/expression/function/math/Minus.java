package sheetimpl.expression.function.math;

import api.EffectiveValue;
import sheetimpl.expression.type.BinaryExpression;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Minus extends BinaryExpression {
    public Minus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right) {
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue = right.evaluate();
        // do some checking... error handling...

        double result = leftValue.extractValueWithExpectation(Double.class) - rightValue.extractValueWithExpectation(Double.class);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}



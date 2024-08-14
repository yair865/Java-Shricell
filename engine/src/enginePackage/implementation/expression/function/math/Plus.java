package enginePackage.implementation.expression.function.math;

import enginePackage.implementation.expression.type.BinaryExpression;
import enginePackage.api.Expression;
import enginePackage.api.EffectiveValue;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

public class Plus extends BinaryExpression {

    public Plus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right) {
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue = right.evaluate();
        // do some checking... error handling...

        double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

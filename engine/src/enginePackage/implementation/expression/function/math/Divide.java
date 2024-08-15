package enginePackage.implementation.expression.function.math;

import enginePackage.api.EffectiveValue;
import enginePackage.implementation.expression.type.BinaryExpression;
import enginePackage.api.Expression;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

public class Divide extends BinaryExpression {
    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right) {
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue = right.evaluate();

        double numerator = leftValue.extractValueWithExpectation(Double.class);
        double denominator = leftValue.extractValueWithExpectation(Double.class);

        if(denominator == 0){
            return new EffectiveValueImpl(CellType.NUMERIC , Double.NaN);
        }

        double result = numerator / denominator; // divide by zero check

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

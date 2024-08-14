package enginePackage.implementation.expression.function.string;

import enginePackage.api.EffectiveValue;
import enginePackage.implementation.expression.type.BinaryExpression;

import enginePackage.api.Expression;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

public class Concat extends BinaryExpression {

    public Concat(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right) {
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue = right.evaluate();
        // do some checking... error handling...

        String result = leftValue.extractValueWithExpectation(String.class) + rightValue.extractValueWithExpectation(String.class);

        return new EffectiveValueImpl(CellType.STRING, result);
    }
}

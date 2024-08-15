package enginePackage.implementation.expression.function.string;

import enginePackage.api.EffectiveValue;
import enginePackage.implementation.expression.type.TrinaryExpression;
import enginePackage.api.Expression;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

public class Sub extends TrinaryExpression {

    public Sub(Expression sourceString, Expression startIndex, Expression endIndex) {
        super(sourceString, startIndex, endIndex);
    }

    @Override
    protected EffectiveValue evaluate(Expression sourceString, Expression startIndex, Expression endIndex) {
        EffectiveValue sourceStringValue = sourceString.evaluate();
        EffectiveValue startIndexValue = startIndex.evaluate();
        EffectiveValue endIndexValue = endIndex.evaluate();

        String source = sourceStringValue.extractValueWithExpectation(String.class);
        Integer start = startIndexValue.extractValueWithExpectation(Integer.class);
        Integer end = endIndexValue.extractValueWithExpectation(Integer.class);

        if (start < 0 || end > source.length() || start > end) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!") ;
        }

        String result = source.substring(start, end);

        return new EffectiveValueImpl(CellType.STRING, result);
    }

}


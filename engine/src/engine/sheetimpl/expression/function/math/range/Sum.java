package engine.sheetimpl.expression.function.math.range;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.UnlimitedExpression;
import engine.sheetimpl.utils.CellType;

import java.util.List;

public class Sum extends UnlimitedExpression {

    public Sum(List<Expression> expressions) {
        super(expressions);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheet);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheet);

        Double leftNumber = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double rightNumber = rightEffectiveValue.extractValueWithExpectation(Double.class);


        if(leftNumber == null || rightNumber == null) {
            return new EffectiveValueImpl(CellType.ERROR, Double.NaN);
        }

        double result = leftNumber + rightNumber;

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}

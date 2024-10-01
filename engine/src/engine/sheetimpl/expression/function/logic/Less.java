package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.BinaryExpression;
import engine.sheetimpl.utils.CellType;

public class Less extends BinaryExpression {

    public Less(Expression arg1, Expression arg2) {
        super(arg1, arg2);
    }

    @Override
    public EffectiveValue evaluate(Expression arg1, Expression arg2, SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = arg1.evaluate(spreadsheet);

        EffectiveValue rightEffectiveValue = arg2.evaluate(spreadsheet);

        Double leftNumber = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double rightNumber = rightEffectiveValue.extractValueWithExpectation(Double.class);

        if (leftNumber == null || rightNumber == null) {
            return new EffectiveValueImpl(CellType.ERROR, "UNKNOWN");
        }
        boolean result = leftNumber <= rightNumber;

        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}

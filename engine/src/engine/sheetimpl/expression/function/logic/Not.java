package engine.sheetimpl.expression.function.logic;

import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.utils.CellType;
import engine.sheetimpl.expression.type.UnaryExpression;

public class Not extends UnaryExpression {

    public Not(Expression exp1) {
        super(exp1);
    }

    @Override
    public EffectiveValue evaluate(Expression exp1, SheetReadActions spreadsheet) {
        EffectiveValue effectiveValue = exp1.evaluate(spreadsheet);

        Boolean boolValue = effectiveValue.extractValueWithExpectation(Boolean.class);

        if (boolValue == null) {
            return new EffectiveValueImpl(CellType.ERROR, "UNKNOWN");
        }

        boolean result = !boolValue;

        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        // The result of the NOT operation is always a BOOLEAN
        return CellType.BOOLEAN;
    }
}

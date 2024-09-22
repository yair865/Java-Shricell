package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
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
            return new EffectiveValueImpl(CellType.ERROR, "Argument must be a boolean");
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

package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.utils.CellType;
import engine.sheetimpl.expression.type.BinaryExpression;

public class Or extends BinaryExpression {

    public Or(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public EffectiveValue evaluate(Expression exp1, Expression exp2, SheetReadActions spreadsheet) {
        EffectiveValue effectiveValue1 = exp1.evaluate(spreadsheet);
        EffectiveValue effectiveValue2 = exp2.evaluate(spreadsheet);

        Boolean boolValue1 = effectiveValue1.extractValueWithExpectation(Boolean.class);
        Boolean boolValue2 = effectiveValue2.extractValueWithExpectation(Boolean.class);
        if (boolValue1 == null || boolValue2 == null) {
            return new EffectiveValueImpl(CellType.ERROR, "!UNDEFINED!");
        }

        // Perform the OR operation
        boolean result = boolValue1 || boolValue2;

        // Return the result as an EffectiveValue with BOOLEAN type
        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        // The result of the OR operation is always a BOOLEAN
        return CellType.BOOLEAN;
    }
}

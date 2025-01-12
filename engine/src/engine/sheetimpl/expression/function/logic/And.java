package engine.sheetimpl.expression.function.logic;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;
import engine.sheetimpl.expression.type.BinaryExpression;
import dto.dtoPackage.CellType;

public class And extends BinaryExpression {

    public And(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public EffectiveValue evaluate(Expression exp1, Expression exp2, SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = exp1.evaluate(spreadsheet);
        EffectiveValue rightEffectiveValue = exp2.evaluate(spreadsheet);

        Boolean leftBoolean = leftEffectiveValue.extractValueWithExpectation(Boolean.class);
        Boolean rightBoolean = rightEffectiveValue.extractValueWithExpectation(Boolean.class);

        if (leftBoolean == null || rightBoolean == null) {
            return new EffectiveValueImpl(CellType.ERROR, "UNKNOWN");
        }

        boolean result = leftBoolean && rightBoolean;

        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        // The result of the AND operation is always a BOOLEAN
        return CellType.BOOLEAN;
    }
}

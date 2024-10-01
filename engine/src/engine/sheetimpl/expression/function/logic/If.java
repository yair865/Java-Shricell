package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.TrinaryExpression;
import engine.sheetimpl.utils.CellType;

public class If extends TrinaryExpression {

    public If(Expression condition, Expression thenExpression, Expression elseExpression) {
        super(condition, thenExpression, elseExpression);
    }

    @Override
    protected EffectiveValue evaluate(Expression condition, Expression thenExpression, Expression elseExpression, SheetReadActions spreadsheet) {
        EffectiveValue conditionEffectiveValue = condition.evaluate(spreadsheet);

        Boolean conditionResult = conditionEffectiveValue.extractValueWithExpectation(Boolean.class);

        if (conditionResult == null) {
            return new EffectiveValueImpl(CellType.ERROR, "UNKNOWN");
        }

        EffectiveValue result;
        if (conditionResult) {
            result = thenExpression.evaluate(spreadsheet);
        } else {
            result = elseExpression.evaluate(spreadsheet);
        }

        return result;
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}

package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.BinaryExpression;
import engine.sheetimpl.utils.CellType;

public class Equal extends BinaryExpression {

    public Equal(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right, SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheet);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheet);

        CellType leftType = leftEffectiveValue.getCellType();
        CellType rightType = rightEffectiveValue.getCellType();

        // If the types are different, return FALSE
        if (!leftType.equals(rightType)) {
            return new EffectiveValueImpl(CellType.BOOLEAN, false);
        }

        // Check the values based on their types
        Object leftValue = leftEffectiveValue.getValue();
        Object rightValue = rightEffectiveValue.getValue();

        boolean isEqual;

        switch (leftType) {
            case NUMERIC:
                isEqual = leftValue instanceof Double && rightValue instanceof Double &&
                        leftValue.equals(rightValue);
                break;
            case STRING:
                isEqual = leftValue instanceof String && rightValue instanceof String &&
                        leftValue.equals(rightValue);
                break;
            case BOOLEAN:
                isEqual = leftValue instanceof Boolean && rightValue instanceof Boolean &&
                        leftValue.equals(rightValue);
                break;
            default:
                return new EffectiveValueImpl(CellType.ERROR, "UNKNOWN");
        }

        return new EffectiveValueImpl(CellType.BOOLEAN, isEqual);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}

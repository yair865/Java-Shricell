package engine.sheetimpl.expression.function.logic;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.BinaryExpression;
import engine.sheetimpl.utils.CellType;

public class And extends BinaryExpression {

    public And(Expression exp1, Expression exp2) {
        super(exp1, exp2);
    }

    @Override
    public EffectiveValue evaluate(Expression exp1, Expression exp2, SheetReadActions spreadsheet) {
        // Evaluate the first expression
        EffectiveValue leftEffectiveValue = exp1.evaluate(spreadsheet);
        // Evaluate the second expression
        EffectiveValue rightEffectiveValue = exp2.evaluate(spreadsheet);

        // Extract the boolean values from the evaluated expressions
        Boolean leftBoolean = leftEffectiveValue.extractValueWithExpectation(Boolean.class);
        Boolean rightBoolean = rightEffectiveValue.extractValueWithExpectation(Boolean.class);

        // If either of the expressions is not a boolean, return an error
        if (leftBoolean == null || rightBoolean == null) {
            return new EffectiveValueImpl(CellType.ERROR, "Arguments must be of boolean type");
        }

        // Perform the AND operation
        boolean result = leftBoolean && rightBoolean;

        // Return the result as an EffectiveValue with BOOLEAN type
        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        // The result of the AND operation is always a BOOLEAN
        return CellType.BOOLEAN;
    }
}

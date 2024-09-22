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
        // Evaluate the first expression
        EffectiveValue leftEffectiveValue = arg1.evaluate(spreadsheet);
        // Evaluate the second expression
        EffectiveValue rightEffectiveValue = arg2.evaluate(spreadsheet);

        // Extract the numeric values from the evaluated expressions
        Double leftNumber = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double rightNumber = rightEffectiveValue.extractValueWithExpectation(Double.class);

        // If either of the expressions is not a number, return an error
        if (leftNumber == null || rightNumber == null) {
            return new EffectiveValueImpl(CellType.ERROR, "Arguments must be numbers");
        }

        // Perform the <= comparison
        boolean result = leftNumber <= rightNumber;

        // Return the result as an EffectiveValue with BOOLEAN type
        return new EffectiveValueImpl(CellType.BOOLEAN, result);
    }

    @Override
    public CellType getFunctionResultType() {
        // The result of the LESS operation is always a BOOLEAN
        return CellType.BOOLEAN;
    }
}

package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.BinaryExpression;
import sheetimpl.utils.CellType;

public class Divide extends BinaryExpression {
    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheetDTO);

        Double numerator = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double denominator = rightEffectiveValue.extractValueWithExpectation(Double.class);


        if(numerator == null || denominator == null) {
            throw new IllegalArgumentException("Invalid arguments in function " + this.getClass().getSimpleName() + ".\n"
                    + "the arguments expected are from type " + CellType.NUMERIC + " but the first argument is from type - " + leftEffectiveValue.getCellType()
                    + ", and the second argument is from type - " + rightEffectiveValue.getCellType() + ".");
        }

        if(denominator == 0){
            return new EffectiveValueImpl(CellType.NUMERIC , Double.NaN);
        }

        double result = numerator / denominator; // divide by zero check

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

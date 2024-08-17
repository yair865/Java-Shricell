package sheetimpl.expression.function.math;

import api.EffectiveValue;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.BinaryExpression;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Divide extends BinaryExpression {
    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightValue = right.evaluate(spreadsheetDTO);

        double numerator = leftValue.extractValueWithExpectation(Double.class);
        double denominator = leftValue.extractValueWithExpectation(Double.class);

        if(denominator == 0){
            return new EffectiveValueImpl(CellType.NUMERIC , Double.NaN);
        }

        double result = numerator / denominator; // divide by zero check

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

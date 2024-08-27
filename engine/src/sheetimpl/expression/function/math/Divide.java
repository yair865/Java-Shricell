package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.BinaryExpression;
import sheetimpl.utils.CellType;

public class Divide extends BinaryExpression {
    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheet);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheet);

        Double numerator = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double denominator = rightEffectiveValue.extractValueWithExpectation(Double.class);


        if(numerator == null || denominator == null) {
            return new EffectiveValueImpl(CellType.ERROR,Double.NaN);
        }

        if(denominator == 0){
            return new EffectiveValueImpl(CellType.ERROR , Double.NaN);
        }

        double result = numerator / denominator;

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}

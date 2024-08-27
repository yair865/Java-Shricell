package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.BinaryExpression;
import sheetimpl.utils.CellType;

public class Pow extends BinaryExpression {

    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }


    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SheetReadActions spreadsheet) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheet);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheet);

        Double leftNumber = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double rightNumber = rightEffectiveValue.extractValueWithExpectation(Double.class);


        if(leftNumber == null || rightNumber == null) {
            return new EffectiveValueImpl(CellType.ERROR, Double.NaN);
        }

        double result = Math.pow(leftNumber, rightNumber);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
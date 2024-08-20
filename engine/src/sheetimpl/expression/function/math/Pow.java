package sheetimpl.expression.function.math;

import api.EffectiveValue;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.BinaryExpression;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Pow extends BinaryExpression {

    public Pow(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }


    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheetDTO);

        Double leftNumber = leftEffectiveValue.extractValueWithExpectation(Double.class);
        Double rightNumber = rightEffectiveValue.extractValueWithExpectation(Double.class);


        if(leftNumber == null || rightNumber == null) {
            throw new IllegalArgumentException("Invalid arguments in function " + this.getClass().getSimpleName() + ".\n"
                    + "the arguments expected are from type " + Number.class.getSimpleName() + " but the first argument is from type - " + leftEffectiveValue.getCellType()
                    + ", and the second argument is from type - " + rightEffectiveValue.getCellType() + ".");
        }

        double result = Math.pow(leftNumber, rightNumber);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}
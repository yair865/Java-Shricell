package sheetimpl.expression.function.math;

import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.BinaryExpression;
import api.Expression;
import api.EffectiveValue;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Plus extends BinaryExpression {

    public Plus(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightValue = right.evaluate(spreadsheetDTO);

        // do some checking... error handling...

        double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

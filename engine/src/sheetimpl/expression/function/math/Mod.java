package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.BinaryExpression;
import sheetimpl.utils.CellType;

public class Mod extends BinaryExpression {
    public Mod(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightValue = right.evaluate(spreadsheetDTO);

        // do some checking... error handling...

        double result = leftValue.extractValueWithExpectation(Double.class) % rightValue.extractValueWithExpectation(Double.class);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}

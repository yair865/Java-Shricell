package sheetimpl.expression.function.string;

import api.EffectiveValue;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.BinaryExpression;

import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Concat extends BinaryExpression {

    public Concat(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right, SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightValue = right.evaluate(spreadsheetDTO);
        // do some checking... error handling...

        String result = leftValue.extractValueWithExpectation(String.class) + rightValue.extractValueWithExpectation(String.class);

        return new EffectiveValueImpl(CellType.STRING, result);
    }
}

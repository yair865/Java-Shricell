package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Spreadsheet;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.UnaryExpression;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SpreadsheetDTO spreadsheetDTO) {

        EffectiveValue expressionValue = expression.evaluate(spreadsheetDTO);

        // do some checking... error handling...

        double result = Math.abs(expressionValue.extractValueWithExpectation(Double.class));

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

}


package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.UnaryExpression;
import sheetimpl.utils.CellType;

public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SheetReadActions spreadsheet) {

        EffectiveValue expressionEffectiveValue = expression.evaluate(spreadsheet);
       Double valueToAbs = expressionEffectiveValue.extractValueWithExpectation(Double.class);

        if(valueToAbs == null) {
            return new EffectiveValueImpl(CellType.ERROR, Double.NaN);
        }

        double result = Math.abs(valueToAbs);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}


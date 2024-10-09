package engine.sheetimpl.expression.function.math;

import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.UnaryExpression;
import engine.sheetimpl.utils.CellType;

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


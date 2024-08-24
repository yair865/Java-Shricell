package sheetimpl.expression.function.math;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.UnaryExpression;
import sheetimpl.utils.CellType;

public class Abs extends UnaryExpression {

    public Abs(Expression expression) {
        super(expression);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SpreadsheetDTO spreadsheetDTO) {

        EffectiveValue expressionEffectiveValue = expression.evaluate(spreadsheetDTO);
       Double valueToAbs = expressionEffectiveValue.extractValueWithExpectation(Double.class);

        if(valueToAbs == null) {
            throw new IllegalArgumentException("Invalid argument in function " + this.getClass().getSimpleName() + ".\n"
                    + "the argument expected is from type " + CellType.NUMERIC + " but the argument is from type - " + expressionEffectiveValue.getCellType() + ".");
        }

        double result = Math.abs(valueToAbs);

        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }

}


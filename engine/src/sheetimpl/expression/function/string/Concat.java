package sheetimpl.expression.function.string;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.expression.type.BinaryExpression;
import sheetimpl.expression.type.Text;
import sheetimpl.utils.CellType;

public class Concat extends BinaryExpression {

    public Concat(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public EffectiveValue evaluate(Expression left, Expression right, SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue leftEffectiveValue = left.evaluate(spreadsheetDTO);
        EffectiveValue rightEffectiveValue = right.evaluate(spreadsheetDTO);

        String leftString = leftEffectiveValue.extractValueWithExpectation(String.class);
        String rightString = rightEffectiveValue.extractValueWithExpectation(String.class);

        if (leftString == null || rightString == null) {
            throw new IllegalArgumentException("Invalid arguments in function " + this.getClass().getSimpleName() + ".\n"
                    + "the arguments expected are from type " + Text.class.getSimpleName() + " but the first argument is from type - " + leftEffectiveValue.getCellType()
                    + ", and the second argument is from type - " + rightEffectiveValue.getCellType() + ".");
        }

        String result = leftString + rightString;

        return new EffectiveValueImpl(CellType.STRING, result);
    }
}

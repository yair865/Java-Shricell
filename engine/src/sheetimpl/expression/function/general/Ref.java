package sheetimpl.expression.function.general;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.UnaryExpression;

public class Ref extends UnaryExpression { // TODO

    public Ref(Expression cellId) {
    super(cellId);
    }

    @Override
    public EffectiveValue evaluate(Expression cellId) {

        return null;
    }

    @Override
    public EffectiveValue evaluate(SpreadsheetDTO spreadsheetDTO) {
        return null;
    }
}

package sheetimpl.expression.function.general;

import api.EffectiveValue;
import api.Expression;
import sheetimpl.expression.type.UnaryExpression;

public class Ref extends UnaryExpression { // TODO

    public Ref(Expression cellId) {
    super(cellId);
    }

    @Override
    public EffectiveValue evaluate(Expression cellId) {
    //maybe move to SheetImpl
        return null;
    }
}

package enginePackage.implementation.expression.function.general;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;
import enginePackage.implementation.expression.type.UnaryExpression;

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

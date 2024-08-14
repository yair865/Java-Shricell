package enginePackage.implementation.expression.type;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;

public class Bool implements Expression {
    private final boolean boolValue;

    public Bool(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public EffectiveValue evaluate() {
        return boolValue;
    }//FIX
}

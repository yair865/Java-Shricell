package enginePackage.implementation.expression.type;

import enginePackage.interfaces.Expression;

public class Bool implements Expression<Boolean> {
    private final boolean boolValue;

    public Bool(boolean boolValue) {
        this.boolValue = boolValue;
    }
    @Override
    public Boolean evaluate() {
        return boolValue;
    }
}

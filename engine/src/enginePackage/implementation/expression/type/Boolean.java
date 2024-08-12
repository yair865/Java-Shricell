package enginePackage.implementation.expression.type;

import enginePackage.interfaces.Expression;

public class Boolean implements Expression<java.lang.Boolean> {

    private boolean boolValue;

    public Boolean(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public java.lang.Boolean evaluate() {
        return boolValue;
    }
}

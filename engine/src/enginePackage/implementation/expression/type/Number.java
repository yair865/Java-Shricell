package enginePackage.implementation.expression.type;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;

public class Number implements Expression {
    private final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public EffectiveValue evaluate() {
        return num;
    } //FIX
}
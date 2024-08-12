package enginePackage.implementation.expression.type;

import enginePackage.interfaces.Expression;

public class Number implements Expression<Double> {
    private final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public Double evaluate() {
        return num;
    }
}
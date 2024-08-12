package enginePackage.implementation.expression.type;

import enginePackage.interfaces.Expression;

public class Number implements Expression<Double> {

    private double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public Double evaluate() {
        return num;
    }

    @Override
    public String toString() {
        return num < 0 ?
                "(" + num + ")" :
                Double.toString(num);
    }

}
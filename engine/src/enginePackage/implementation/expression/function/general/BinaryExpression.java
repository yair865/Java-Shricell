package enginePackage.implementation.expression.function.general;

import enginePackage.interfaces.Expression;


public abstract class BinaryExpression<T> implements Expression<T> {

    private Expression<T> expression1;
    private Expression<T> expression2;

    public BinaryExpression(Expression<T> expression1, Expression<T> expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public T evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate());
    }

    abstract protected T evaluate(T evaluate, T evaluate2);
}



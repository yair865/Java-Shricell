package enginePackage.implementation.expression.function.general;

import enginePackage.interfaces.Expression;


public abstract class UnaryExpression<T> implements Expression<T> {

    private Expression<T> expression;

    public UnaryExpression(Expression<T> expression) {
        this.expression = expression;
    }
    @Override
    public T evaluate() {
        return evaluate(expression.evaluate());
    }

    abstract protected T evaluate(T evaluate);
}
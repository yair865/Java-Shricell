package enginePackage.implementation.expression.function.general;

import enginePackage.interfaces.Expression;

public abstract class TrinaryExpression<T> implements Expression<T> {

    private Expression<T> expression1;
    private Expression<T> expression2;
    private Expression<T> expression3;

    public TrinaryExpression(Expression<T> expression1, Expression<T> expression2 , Expression<T> expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public T evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate() , expression3.evaluate());
    }

    abstract protected T evaluate(T evaluate, T evaluate2 , T evaluate3);
}
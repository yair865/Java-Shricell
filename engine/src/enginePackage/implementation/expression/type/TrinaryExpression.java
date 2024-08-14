package enginePackage.implementation.expression.type;

import enginePackage.api.Expression;

public abstract class TrinaryExpression<T,R> implements Expression<T> {

    private final Expression<T> expression1;
    private final Expression<T> expression2;
    private final Expression<R> expression3;

    public TrinaryExpression(Expression<T> expression1, Expression<T> expression2 , Expression<R> expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public T evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate() , expression3.evaluate());
    }

    abstract protected T evaluate(T evaluate, T evaluate2 , R evaluate3);
}
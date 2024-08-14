package enginePackage.implementation.expression.type;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;


public abstract class UnaryExpression implements Expression {

    private final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }
    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression);
    }

    public abstract EffectiveValue evaluate(Expression expression);
}
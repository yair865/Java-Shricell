package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;


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
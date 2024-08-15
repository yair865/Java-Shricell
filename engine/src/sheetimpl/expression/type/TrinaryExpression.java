package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;

public abstract class TrinaryExpression implements Expression {

    private final Expression expression1;
    private final Expression expression2;
    private final Expression expression3;

    public TrinaryExpression(Expression expression1, Expression expression2 , Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression1, expression2 , expression3);
    }

    abstract protected EffectiveValue evaluate(Expression evaluate, Expression evaluate2 , Expression evaluate3);
}
package engine.sheetimpl.expression.type;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;

import java.util.List;


public abstract class UnlimitedExpression implements Expression {

    private List<Expression> expressions;


    public UnlimitedExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadsheet) {
        return evaluate(expressions.get(0), expressions.get(1), spreadsheet);
    }

    public abstract EffectiveValue evaluate(Expression left, Expression right, SheetReadActions spreadsheet);

}



package engine.sheetimpl.expression.type;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;


public abstract class UnaryExpression implements Expression {

    private final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadsheet) {
        return evaluate(expression, spreadsheet);
    }

    public abstract EffectiveValue evaluate(Expression expression,SheetReadActions spreadsheet);
}
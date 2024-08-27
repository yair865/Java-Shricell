package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;


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
package engine.sheetimpl.expression.type;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;


public abstract class BinaryExpression implements Expression {

    private final Expression expression1;
    private final Expression expression2;

    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadsheet) {
        return evaluate(expression1, expression2, spreadsheet);
    }

    public abstract EffectiveValue evaluate(Expression left, Expression right, SheetReadActions spreadsheet);

}



package engine.sheetimpl.expression.type;

import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.utils.CellType;

public class Text implements Expression {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadSheet) {
        return new EffectiveValueImpl(CellType.STRING, text);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}

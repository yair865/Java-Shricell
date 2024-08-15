package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Text implements Expression {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public EffectiveValue evaluate() {
        return new EffectiveValueImpl(CellType.STRING, text);
    }
}

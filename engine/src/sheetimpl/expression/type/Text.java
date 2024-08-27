package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

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

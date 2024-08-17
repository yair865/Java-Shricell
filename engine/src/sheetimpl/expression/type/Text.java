package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import api.Spreadsheet;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Text implements Expression {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public EffectiveValue evaluate(SpreadsheetDTO spreadSheet) {
        return new EffectiveValueImpl(CellType.STRING, text);
    }
}

package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Bool implements Expression {
    private final boolean boolValue;

    public Bool(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public EffectiveValue evaluate(SpreadsheetDTO spreadsheetDTO) {

        return new EffectiveValueImpl(CellType.BOOLEAN, boolValue);
    }//FIX
}

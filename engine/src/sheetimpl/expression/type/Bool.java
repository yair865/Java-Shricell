package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Bool implements Expression {
    private final boolean boolValue;

    public Bool(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadsheet) {

        return new EffectiveValueImpl(CellType.BOOLEAN, boolValue);
    }//FIX

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}

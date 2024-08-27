package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import api.SheetReadActions;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Number implements Expression {
    private final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadsheet) {

        return new EffectiveValueImpl(CellType.NUMERIC, num);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Number implements Expression {
    private final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public EffectiveValue evaluate(SpreadsheetDTO spreadsheetDTO) {

        return new EffectiveValueImpl(CellType.NUMERIC, num);
    }
}
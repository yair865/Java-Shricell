package engine.sheetimpl.expression.type;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;
import dto.dtoPackage.CellType;

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

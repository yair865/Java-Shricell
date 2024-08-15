package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Bool implements Expression {
    private final boolean boolValue;

    public Bool(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public EffectiveValue evaluate() {

        return new EffectiveValueImpl(CellType.BOOLEAN, boolValue);
    }//FIX
}

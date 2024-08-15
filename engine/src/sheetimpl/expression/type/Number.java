package sheetimpl.expression.type;

import api.EffectiveValue;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Number implements Expression {
    private final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public EffectiveValue evaluate() {

        return new EffectiveValueImpl(CellType.NUMERIC, num);
    }
}
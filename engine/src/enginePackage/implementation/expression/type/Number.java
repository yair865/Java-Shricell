package enginePackage.implementation.expression.type;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;
import enginePackage.implementation.physicalParts.cell.EffectiveValueImpl;
import enginePackage.implementation.utils.CellType;

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
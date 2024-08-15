package enginePackage.implementation.physicalParts.cell;

import enginePackage.api.EffectiveValue;
import enginePackage.implementation.utils.CellType;

public class EffectiveValueImpl implements EffectiveValue {

    private CellType cellType;
    private Object value;

    public EffectiveValueImpl(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T extractValueWithExpectation(Class<T> type) { //note: Optional as return value.
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        // error handling... exception ? return null ?
        return null;
    }
}
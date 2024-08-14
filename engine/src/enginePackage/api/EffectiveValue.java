package enginePackage.api;

import enginePackage.implementation.utils.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    <T> T extractValueWithExpectation(Class<T> type);
}

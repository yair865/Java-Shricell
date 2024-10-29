package dto.dtoPackage.effectivevalue;

import dto.dtoPackage.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    <T> T extractValueWithExpectation(Class<T> type);
}

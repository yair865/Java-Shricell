package engine.sheetimpl.cellimpl.api;

import dto.dtoPackage.effectivevalue.EffectiveValue;

public interface CellWriteActions {
    void setCellOriginalValue(String value);
    void setEffectiveValue(EffectiveValue effectiveValue);
    void setLastModifiedVersion(int sheetVersion);
    void setReviserName(String reviserName);
    void setTextColor(String textColor);
    void setBackgroundColor(String backgroundColor);
}

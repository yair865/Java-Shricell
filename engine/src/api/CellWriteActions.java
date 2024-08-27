package api;

public interface CellWriteActions {
    void setCellOriginalValue(String value);
    void setEffectiveValue(EffectiveValue effectiveValue);
    void setLastModifiedVersion(int sheetVersion);
}

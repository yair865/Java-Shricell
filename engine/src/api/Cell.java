package api;

public interface Cell {

    String getOriginalValue();
    void setCellOriginalValue(String value);
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
    void setEffectiveValue(EffectiveValue effectiveValue);
    void setLastModifiedVersion(int sheetVersion);

    void setModified(boolean modified);
}

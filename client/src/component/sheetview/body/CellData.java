package component.sheetview.body;

public interface CellData {

    String getEffectiveValue();

    void setEffectiveValue(String effectiveValue);

    String getOriginalValue();

    void setOriginalValue(String originalValue);

    String getCellId();

    void setCellId(String cellId);

    void setLastModifiedVersion(int lastModifiedVersion);

    int getLastModifiedVersion();
}

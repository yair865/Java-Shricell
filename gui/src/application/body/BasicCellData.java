package application.body;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BasicCellData {
    protected  StringProperty effectiveValue;
    protected  StringProperty originalValue;
    protected  StringProperty cellId;
    protected  IntegerProperty lastModifiedVersion;

    public BasicCellData() {
        effectiveValue = new SimpleStringProperty();
        originalValue = new SimpleStringProperty();
        cellId = new SimpleStringProperty();
        lastModifiedVersion = new SimpleIntegerProperty();
    }

    public BasicCellData(String effectiveValue, String originalValue, String cellId) {
        this.effectiveValue = new SimpleStringProperty(effectiveValue);
        this.originalValue = new SimpleStringProperty(originalValue);
        this.cellId = new SimpleStringProperty(cellId);
        this.lastModifiedVersion = new SimpleIntegerProperty(1); // Initialize with default value
    }

    public StringProperty effectiveValueProperty() {
        return effectiveValue;
    }

    public StringProperty originalValueProperty() {
        return originalValue;
    }

    public StringProperty cellIdProperty() {
        return cellId;
    }

    public IntegerProperty lastModifiedVersionProperty() {
        return lastModifiedVersion;
    }

    public String getEffectiveValue() {
        return effectiveValue.get();
    }

    public void setEffectiveValue(String value) {
        effectiveValue.set(value);
    }

    public String getOriginalValue() {
        return originalValue.get();
    }

    public void setOriginalValue(String value) {
        originalValue.set(value);
    }

    public int getLastModifiedVersion() {
        return lastModifiedVersion.get();
    }

    public void setLastModifiedVersion(int version) {
        lastModifiedVersion.set(version);
    }
}

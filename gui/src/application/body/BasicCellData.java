package application.body;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class BasicCellData {
    protected  StringProperty effectiveValue;
    protected  StringProperty originalValue;
    protected  StringProperty cellId;
    protected  IntegerProperty lastModifiedVersion;
    protected  Color textColor;
    protected  Color backgroundColor;

    public BasicCellData() {
        effectiveValue = new SimpleStringProperty();
        originalValue = new SimpleStringProperty();
        cellId = new SimpleStringProperty();
        lastModifiedVersion = new SimpleIntegerProperty();
        textColor = Color.BLACK;
        backgroundColor = Color.WHITE;
    }

    public BasicCellData(String effectiveValue, String originalValue, String cellId,String textColor, String backgroundColor) {
        this.effectiveValue = new SimpleStringProperty(effectiveValue);
        this.originalValue = new SimpleStringProperty(originalValue);
        this.cellId = new SimpleStringProperty(cellId);
        this.lastModifiedVersion = new SimpleIntegerProperty(1);
        this.textColor = Color.web(textColor);
        this.backgroundColor = Color.web(backgroundColor);
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

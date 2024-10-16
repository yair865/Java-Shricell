package component.sheetview.body;

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
    protected  StringProperty textColor;
    protected  StringProperty backgroundColor;

    public BasicCellData(String effectiveValue, String originalValue, String cellId,String textColor, String backgroundColor) {
        this.effectiveValue = new SimpleStringProperty(effectiveValue);
        this.originalValue = new SimpleStringProperty(originalValue);
        this.cellId = new SimpleStringProperty(cellId);
        this.lastModifiedVersion = new SimpleIntegerProperty(1);
        this.textColor = new SimpleStringProperty(textColor);
        this.backgroundColor = new SimpleStringProperty(backgroundColor);
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

    public void setTextColor(Color textColor) {
        this.textColor = toHexString(textColor);
    }

    public void setBackGroundColor(Color backgroundColor) {
        this.backgroundColor = toHexString(backgroundColor);
    }

    private StringProperty toHexString(Color color) {
        String hexColor = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        return new SimpleStringProperty(hexColor);
    }
}

package engine.sheetimpl.cellimpl;

import engine.api.Cell;
import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.sheetimpl.utils.CellType;

import java.io.Serializable;

public class CellImpl implements Cell , Serializable {

    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final Coordinate coordinate;
    private CellStyle cellStyle;
    

    public CellImpl(Coordinate coordinate) {
        this.originalValue = "";
        this.effectiveValue = new EffectiveValueImpl(CellType.STRING , "");
        this.lastModifiedVersion = 1;
        this.coordinate = coordinate;
        this.cellStyle = new CellStyle();
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setCellOriginalValue(String value) {
        this.originalValue = value;
    }

    @Override
    public EffectiveValue getEffectiveValue() {
        return effectiveValue;
    }

    @Override
    public int getLastModifiedVersionVersion() {
        return lastModifiedVersion;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public void setEffectiveValue(EffectiveValue newEffectiveValue) {this.effectiveValue = newEffectiveValue;}

    @Override
    public void setLastModifiedVersion(int sheetVersion) {
        this.lastModifiedVersion = sheetVersion;
    }

    @Override
    public CellStyle getCellStyle() {
        return cellStyle;
    }

    @Override
    public void setTextColor(String textColor) {
        this.cellStyle.setTextColor(textColor);
    }

    @Override
    public void setBackgroundColor(String backgroundColor) {
        this.cellStyle.setTextColor(backgroundColor);
    }
}
package engine.sheetimpl.cellimpl;

import engine.sheetimpl.cellimpl.api.Cell;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import dto.dtoPackage.CellType;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;

import java.io.Serializable;

public class CellImpl implements Cell , Serializable {

    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;
    private final Coordinate coordinate;
    private CellStyle cellStyle;
    private String reviserName;

    public CellImpl(Coordinate coordinate) {
        this.originalValue = "";
        this.effectiveValue = new EffectiveValueImpl(CellType.STRING , "");
        this.lastModifiedVersion = 1;
        this.coordinate = coordinate;
        this.cellStyle = new CellStyle();
        this.reviserName = "";
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
    public String getReviserName() {return reviserName;}

    @Override
    public void setReviserName(String reviserName) {this.reviserName = reviserName;}

    @Override
    public boolean getContainsFunction() {
        return originalValue.startsWith("{") && originalValue.endsWith("}");
    }

    @Override
    public void setTextColor(String textColor) {
        this.cellStyle.setTextColor(textColor);
    }

    @Override
    public void setBackgroundColor(String backgroundColor) {
        this.cellStyle.setBackgroundColor(backgroundColor);
    }
}
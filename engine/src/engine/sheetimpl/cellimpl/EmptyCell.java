package engine.sheetimpl.cellimpl;

import engine.sheetimpl.cellimpl.api.Cell;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import dto.dtoPackage.CellType;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;

import java.io.Serializable;

public enum EmptyCell implements Cell, Serializable {

    INSTANCE;

    @Override
    public String getOriginalValue() {
        return "";
    }

    public EffectiveValue getEffectiveValue() {
        return new EffectiveValueImpl(CellType.EMPTY, "");
    }

    @Override
    public int getLastModifiedVersionVersion() {
        return 0;
    }

    @Override
    public Coordinate getCoordinate() {
        return null;
    }

    @Override
    public CellStyle getCellStyle() {
        return null;
    }

    @Override
    public String getReviserName() {
        return "";
    }

    @Override
    public boolean getContainsFunction() {
        return false;
    }

    @Override
    public void setCellOriginalValue(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEffectiveValue(EffectiveValue effectiveValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastModifiedVersion(int sheetVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReviserName(String reviserName) {throw new UnsupportedOperationException();}

    @Override
    public void setTextColor(String textColor) {

    }

    @Override
    public void setBackgroundColor(String backgroundColor) {

    }
}

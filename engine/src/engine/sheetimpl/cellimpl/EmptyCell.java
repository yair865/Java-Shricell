package engine.sheetimpl.cellimpl;

import engine.api.Cell;
import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.sheetimpl.utils.CellType;

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
}

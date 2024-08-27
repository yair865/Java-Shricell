package sheetimpl.cellimpl;

import api.Cell;
import api.EffectiveValue;
import sheetimpl.utils.CellType;

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

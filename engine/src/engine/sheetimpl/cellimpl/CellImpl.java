package engine.sheetimpl.cellimpl;

import engine.api.Cell;
import engine.api.EffectiveValue;
import engine.sheetimpl.utils.CellType;

import java.io.Serializable;

public class CellImpl implements Cell , Serializable {

    private String originalValue;
    private EffectiveValue effectiveValue;
    private int lastModifiedVersion;

    public CellImpl(String originalValue, EffectiveValue effectiveValue, int version) {
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.lastModifiedVersion = version;
    }

    public CellImpl() {
        this.originalValue = "";
        this.effectiveValue = new EffectiveValueImpl(CellType.STRING , "");
        this.lastModifiedVersion = 1;
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
    public void setEffectiveValue(EffectiveValue newEffectiveValue) {this.effectiveValue = newEffectiveValue;}

    @Override
    public void setLastModifiedVersion(int sheetVersion) {
        this.lastModifiedVersion = sheetVersion;
    }

}
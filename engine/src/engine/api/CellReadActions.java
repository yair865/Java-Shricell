package engine.api;

import engine.sheetimpl.cellimpl.CellStyle;

public interface CellReadActions {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
    Coordinate getCoordinate();
    CellStyle getCellStyle();
}

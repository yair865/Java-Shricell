package engine.sheetimpl.cellimpl.api;

import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.CellStyle;

public interface CellReadActions {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
    Coordinate getCoordinate();
    CellStyle getCellStyle();
    String getReviserName();
    boolean getContainsFunction();
}

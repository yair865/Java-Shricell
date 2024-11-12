package engine.sheetimpl.cellimpl.api;

import dto.dtoPackage.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.CellStyle;
import dto.dtoPackage.effectivevalue.EffectiveValue;

public interface CellReadActions {
    String getOriginalValue();
    EffectiveValue getEffectiveValue();
    int getLastModifiedVersionVersion();
    Coordinate getCoordinate();
    CellStyle getCellStyle();
    String getReviserName();
    boolean getContainsFunction();
}

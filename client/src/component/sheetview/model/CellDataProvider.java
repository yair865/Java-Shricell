package component.sheetview.model;

import component.sheetview.body.BasicCellData;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

public interface CellDataProvider {
    BasicCellData getCellData(Coordinate coordinate);
}

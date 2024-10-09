package application.model;

import application.body.BasicCellData;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

public interface CellDataProvider {
    BasicCellData getCellData(Coordinate coordinate);
}

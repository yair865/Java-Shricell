package application.model;

import application.body.BasicCellData;
import engine.api.Coordinate;

public interface CellDataProvider {
    BasicCellData getCellData(Coordinate coordinate);
}

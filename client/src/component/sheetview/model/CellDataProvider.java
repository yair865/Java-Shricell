package component.sheetview.model;

import component.sheetview.body.BasicCellData;
import dto.dtoPackage.coordinate.Coordinate;

public interface CellDataProvider {
    BasicCellData getCellData(Coordinate coordinate);
}

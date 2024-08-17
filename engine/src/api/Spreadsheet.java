package api;

import sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.List;
import java.util.Map;

/**
 * Interface representing a spreadsheet.
 */
public interface Spreadsheet {
    int getRows();

    int getColumns();

    void setTitle(String sheetName);
    String getSheetName();
    int getVersion();
    Cell getCell(Coordinate coordinate);
    void setCell(int row, int column, String value);

    Map<Coordinate, Cell> getActiveCells();

    void setRows(int rows);

    void setColumns(int columns);
}

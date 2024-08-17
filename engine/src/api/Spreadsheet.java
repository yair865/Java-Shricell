package api;

import sheetimpl.cellimpl.coordinate.Coordinate;

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

    void setCell(Coordinate coordinate, String value);

    Map<Coordinate, Cell> getActiveCells();

    void setRows(int rows);

    void setColumns(int columns);

    int getRowHeightUnits();

    void setRowHeightUnits(int rowHeightUnits);

    int getColumnWidthUnits();

    void setColumnWidthUnits(int columnWidthUnits);

    void clearSpreadSheet();
}

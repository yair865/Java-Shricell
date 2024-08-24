package api;

import generated.STLSheet;

import java.util.List;
import java.util.Map;

/**
 * Interface representing a spreadsheet.
 */
public interface Spreadsheet extends SheetReadActions, SheetUpdateActions {
    int getRows();

    int getColumns();

    int getSheetVersion();

    Map<Coordinate, List<Coordinate>> getDependenciesAdjacencyList();

    void setTitle(String sheetName);

    String getSheetName();

    int getVersion();

    Cell getCell(Coordinate coordinate);

    Map<Coordinate, List<Coordinate>> getReferencesAdjacencyList();

    int getNumberOfModifiedCells();

    void setCell(Coordinate coordinate, String value);

    Map<Coordinate, Cell> getActiveCells();

    void setRows(int rows);

    void setColumns(int columns);

    int getRowHeightUnits();

    void setRowHeightUnits(int rowHeightUnits);

    int getColumnWidthUnits();

    void setColumnWidthUnits(int columnWidthUnits);

    Spreadsheet copySheet();

    void init(STLSheet loadedSheetFromXML);

    List<Cell> getCellsThatHaveChanged();

    void setSheetVersion(int sheetVersion);
}

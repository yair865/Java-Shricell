package api;

import java.util.List;
import java.util.Map;

public interface SheetReadActions {

    int getRows();
    int getColumns();
    int getSheetVersion();
    String getSheetName();
    int getVersion();
    CellReadActions getCell(Coordinate coordinate);
    Map<Coordinate, List<Coordinate>> getDependenciesAdjacencyList();
    Map<Coordinate, List<Coordinate>> getReferencesAdjacencyList();
    int getNumberOfModifiedCells();
    Map<Coordinate, Cell> getActiveCells();
    int getRowHeightUnits();
    int getColumnWidthUnits();
    Spreadsheet copySheet();
    List<Cell> getCellsThatHaveChanged();
}


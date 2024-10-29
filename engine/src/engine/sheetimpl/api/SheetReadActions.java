package engine.sheetimpl.api;

import engine.sheetimpl.cellimpl.api.Cell;
import engine.sheetimpl.cellimpl.api.CellReadActions;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.range.Range;

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
    List<Coordinate> getCellsThatHaveChanged();
    Map<String, Range> getRanges();
    Range getRangeByName(String name);
    boolean rangeExists(String rangeName);
    void sortSheet(String cellsRange, List<Character> selectedColumns);
   List<String> getUniqueValuesFromColumn(char columnNumber);
}


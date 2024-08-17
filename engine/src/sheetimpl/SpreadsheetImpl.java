package sheetimpl;

import api.Cell;
import api.Spreadsheet;
import sheetimpl.cellimpl.CellImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.util.HashMap;
import java.util.Map;

public class SpreadsheetImpl implements Spreadsheet {
    private String sheetName;
    private int sheetVersion;
    private Map<Coordinate, Cell> activeCells;
    private int rows;
    private int columns;
    private int rowHeightUnits;
    private int columnWidthUnits;

    public SpreadsheetImpl() {
        this.activeCells = new HashMap<>();
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        validateCoordinateInbound(coordinate , rows, columns);

        return activeCells.get(coordinate);
    }

    public static void validateCoordinateInbound(Coordinate coordinate, int rowsLimit, int columnsLimit){
        if (coordinate.row() < 1 || coordinate.row() > rowsLimit || coordinate.column() < 1 || coordinate.column() > columnsLimit) {
            throw new IllegalArgumentException("Cell at position (" + coordinate.row() + ", " + coordinate.column() +
                    ") is outside the sheet boundaries: max rows = " + rowsLimit +
                    ", max columns = " + columnsLimit);
        }
    }

    @Override
    public Map<Coordinate, Cell> getActiveCells() {
        return activeCells;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
       return this.columns;
    }

    @Override
    public void setTitle(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public void setCell(int row, int column, String value) {
        Coordinate coordinate = CoordinateFactory.createCoordinate(row, column);
        Cell cell = activeCells.computeIfAbsent(coordinate, c -> new CellImpl(c, value, null, 1, null, null));
        cell.setCellOriginalValue(value);
        cell.advanceVersion();
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRowHeightUnits() {
        return rowHeightUnits;
    }

    public void setRowHeightUnits(int rowHeightUnits) {
        this.rowHeightUnits = rowHeightUnits;
    }

    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }

    public void setColumnWidthUnits(int columnWidthUnits) {
        this.columnWidthUnits = columnWidthUnits;
    }
}

package sheetimpl;

import api.Cell;
import api.EffectiveValue;
import api.Spreadsheet;
import sheetimpl.cellimpl.CellImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.utils.ExpressionParser.buildExpressionFromString;

public class SpreadsheetImpl implements Spreadsheet {
    private String sheetName;
    private int sheetVersion;
    private Map<Coordinate, Cell> activeCells;
    private Map<Coordinate, List<Coordinate>> dependencies;
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
    public void setCell(Coordinate coordinate , String value) {
        Cell cell = getCell(coordinate);
        EffectiveValue effectiveValue = buildExpressionFromString(value).evaluate(convertSheetToDTO(this));
        cell.setCellOriginalValue(value);
        cell.setEffectiveValue(buildExpressionFromString(value).evaluate(convertSheetToDTO(this)));
        cell.setLastModifiedVersion(sheetVersion);
        cell = activeCells.computeIfAbsent(coordinate, c -> new CellImpl(value, effectiveValue, sheetVersion));
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
    }
    @Override
    public int getRowHeightUnits() {
        return rowHeightUnits;
    }
    @Override
    public void setRowHeightUnits(int rowHeightUnits) {
        this.rowHeightUnits = rowHeightUnits;
    }
    @Override
    public int getColumnWidthUnits() {
        return columnWidthUnits;
    }
    @Override
    public void setColumnWidthUnits(int columnWidthUnits) {
        this.columnWidthUnits = columnWidthUnits;
    }

    @Override
    public void clearSpreadSheet() {
        activeCells.clear();
        dependencies.clear();
    }
}

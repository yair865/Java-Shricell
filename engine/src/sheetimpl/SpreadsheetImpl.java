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

    public SpreadsheetImpl() {
        this.activeCells = new HashMap<>();
    }

    @Override
    public void setTitle(String sheetName) {
        this.sheetName = sheetName;
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
    public Cell getCell(int row, int column) {
        return activeCells.get(CoordinateFactory.createCoordinate(row, column));
    }

    @Override
    public void setCell(int row, int column, String value) {
        Coordinate coordinate = CoordinateFactory.createCoordinate(row, column);
        Cell cell = activeCells.computeIfAbsent(coordinate, c -> new CellImpl(c, value, null, 1, null, null));
        cell.setCellOriginalValue(value);
        cell.advanceVersion();
    }

    public Map<Coordinate, Cell> getActiveCells() {
        return activeCells;
    }
}

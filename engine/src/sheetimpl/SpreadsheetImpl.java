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
        return 0;
    }

    @Override
    public Cell getCell(int row, int column) {
        return activeCells.get(CoordinateFactory.createCoordinate(row, column));
    }

    @Override
    public void setCell(int row, int column, String value) {
        Coordinate coordinate = CoordinateFactory.createCoordinate(row, column);
        Cell cell = activeCells.get(coordinate);

        // If the cell doesn't exist at the given coordinate, create a new one and add it to the map.
        if (cell == null) {
            cell = new CellImpl(row,column,value,null,1,null,null); // Assuming CellImpl is your implementation of the Cell interface
            activeCells.put(coordinate, cell);
        }

        cell.setCellOriginalValue(value);
        cell.advanceVersion();

    }

    }

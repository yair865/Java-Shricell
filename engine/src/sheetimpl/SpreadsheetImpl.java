package sheetimpl;

import api.Cell;
import api.Spreadsheet;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.util.HashMap;
import java.util.Map;

public class SpreadsheetImpl implements Spreadsheet {

    private Map<Coordinate, Cell> activeCells;

    public SpreadsheetImpl() {
        this.activeCells = new HashMap<>();
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
        // if null need to create the cell...

        cell.setCellOriginalValue(value);
    }

    }

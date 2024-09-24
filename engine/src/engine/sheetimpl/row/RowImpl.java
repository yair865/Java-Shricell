package engine.sheetimpl.row;

import engine.api.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowImpl implements Row , Serializable {
    private final int rowNumber;
    private Map<Character, Double> values;
    private List<Cell> cellsInRow;

    public RowImpl(int rowNumber) {
        this.rowNumber = rowNumber;
        this.values = new HashMap<>();
        this.cellsInRow = new ArrayList<>();
    }

    @Override
    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    public Double getValue(char columnIndex) {
        return values.get(columnIndex);
    }

    public void addCellToRow(Cell cell, Double value) {
        if (cell != null) {

            cellsInRow.add(cell);

            char columnIndex = (char) ('A' + cell.getCoordinate().column() - 1);
            values.put(columnIndex, value);
        }
    }

    public List<Cell> getCellsInRow() {
        return cellsInRow;
    }
}

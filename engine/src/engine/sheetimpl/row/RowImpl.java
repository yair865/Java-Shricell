package engine.sheetimpl.row;

import engine.api.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowImpl implements Row, Serializable {
    private final int rowNumber;
    private Map<Integer, Double> values;
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
    public Double getValue(int columnIndex) {
        return values.get(columnIndex);
    }

    @Override
    public void addCellToRow(Cell cell, Double value) {
        if (cell != null) {
            cellsInRow.add(cell);
            int columnIndex = cell.getCoordinate().column();
            values.put(columnIndex, value);
        }
    }

    @Override
    public List<Cell> getCellsInRow() {
        return cellsInRow;
    }

    @Override
    public Cell getCell(int columnIndex) {
        for (Cell cell : cellsInRow) {
            if (cell.getCoordinate().column() == columnIndex) {
                return cell;
            }
        }
        return null;
    }
}

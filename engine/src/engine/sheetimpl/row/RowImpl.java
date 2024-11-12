package engine.sheetimpl.row;

import engine.sheetimpl.cellimpl.api.Cell;
import dto.dtoPackage.effectivevalue.EffectiveValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowImpl implements Row, Serializable {
    private final int rowNumber;
    private Map<Integer, EffectiveValue> values;
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
    public EffectiveValue getValue(int columnIndex) {
        return values.get(columnIndex);
    }

    @Override
    public void addCellToRow(Cell cell, EffectiveValue effectiveValue) {
        if (cell != null) {
            cellsInRow.add(cell);
            int columnIndex = cell.getCoordinate().column();
            values.put(columnIndex, effectiveValue);
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

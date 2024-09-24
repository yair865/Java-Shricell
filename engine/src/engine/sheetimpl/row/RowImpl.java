package engine.sheetimpl.row;

import engine.api.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowImpl implements Row {
    private final int rowNumber;
    Map<Character,Double> values;
    List<Cell> cellsInRow;

    public RowImpl(int rowNumber) {
        this.rowNumber = rowNumber;
        values = new HashMap<Character,Double>();
        cellsInRow = new ArrayList<Cell>();
    }

    @Override
    public int getRowNumber() {
        return rowNumber;
    }

    @Override
    public Double getValue(char columnIndex) {
        return values.get(columnIndex);
    }

}

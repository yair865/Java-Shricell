package engine.sheetimpl.row;

import engine.api.Cell;
import engine.api.EffectiveValue;

import java.util.List;

public interface Row {

    int getRowNumber();

    EffectiveValue getValue(int columnIndex);


    void addCellToRow(Cell cell, EffectiveValue effectiveValue);

    List<Cell> getCellsInRow();

    Cell getCell(int columnIndex);
}

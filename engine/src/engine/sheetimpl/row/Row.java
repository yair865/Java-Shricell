package engine.sheetimpl.row;

import engine.api.Cell;

import java.util.List;

public interface Row {

    int getRowNumber();

    Double getValue(int columnIndex);

    void addCellToRow(Cell cell, Double value);

    List<Cell> getCellsInRow();

    Cell getCell(int columnIndex);
}

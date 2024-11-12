package engine.sheetimpl.row;

import engine.sheetimpl.cellimpl.api.Cell;
import dto.dtoPackage.effectivevalue.EffectiveValue;

import java.util.List;

public interface Row {

    int getRowNumber();

    EffectiveValue getValue(int columnIndex);


    void addCellToRow(Cell cell, EffectiveValue effectiveValue);

    List<Cell> getCellsInRow();

    Cell getCell(int columnIndex);
}

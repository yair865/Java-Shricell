package engine.sheetimpl.sortfilter;

import engine.api.Coordinate;
import engine.api.Spreadsheet;
import engine.sheetimpl.row.RowImpl;

import java.util.List;

public interface SortManager {
    void sortRowsByColumns(List<Coordinate> rows, List<Character> columnIndices , Spreadsheet spreadsheet);
}

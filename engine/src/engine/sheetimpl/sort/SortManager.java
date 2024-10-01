package engine.sheetimpl.sort;

import engine.api.Coordinate;
import engine.api.Spreadsheet;

import java.util.List;

public interface SortManager {
    void sortRowsByColumns(List<Coordinate> rows, List<Character> columnIndices , Spreadsheet spreadsheet);
}

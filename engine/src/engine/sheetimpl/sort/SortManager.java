package engine.sheetimpl.sort;

import dto.dtoPackage.coordinate.Coordinate;
import engine.sheetimpl.api.Spreadsheet;

import java.util.List;

public interface SortManager {
    void sortRowsByColumns(List<Coordinate> rows, List<Character> columnIndices , Spreadsheet spreadsheet);
}

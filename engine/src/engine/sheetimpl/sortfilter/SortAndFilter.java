package engine.sheetimpl.sortfilter;

import engine.sheetimpl.row.RowImpl;

import java.util.List;

public interface SortAndFilter {
    void sortRowsByColumns(List<RowImpl> rows, List<Character> columnIndices);
}

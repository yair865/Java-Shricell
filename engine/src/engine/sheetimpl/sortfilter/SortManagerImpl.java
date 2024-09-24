package engine.sheetimpl.sortfilter;

import engine.api.Cell;
import engine.api.Coordinate;
import engine.api.Spreadsheet;
import engine.sheetimpl.SpreadsheetImpl;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetimpl.row.RowImpl;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class SortManagerImpl implements SortManager {

    public SortManagerImpl() {
    }

    @Override
    public void sortRowsByColumns(List<Coordinate> rangeCoordinates, List<Character> columnIndices , Spreadsheet spreadsheet) {
        Coordinate start = rangeCoordinates.get(0);
        Coordinate end = rangeCoordinates.get(1);

        List<Integer> columnIndicesToSort = new ArrayList<>();
        for (Character column : columnIndices) {
            int colIndex = parseColumn(column);
            if (colIndex < start.column() || colIndex > end.column()) {
                throw new IllegalArgumentException("Column " + column + " is out of the range.");
            }
            columnIndicesToSort.add(colIndex);
        }

        List<RowImpl> rows = extractRowsInRange(start,  end, columnIndicesToSort , spreadsheet);

    }


    private int parseColumn(char column) {
        return (toUpperCase(column) - 'A' + 1);
    }

    private List<RowImpl> extractRowsInRange(Coordinate start, Coordinate end, List<Integer> columnIndicesToSort , Spreadsheet spreadsheet) {
        List<RowImpl> rows = new ArrayList<>();

        for (int row = start.row(); row <= end.row(); row++) {
            RowImpl rowImpl = new RowImpl(row);
            boolean isEmptyRow = true;

            for (int col : columnIndicesToSort) {
                    Coordinate cellCoordinate = CoordinateFactory.createCoordinate(row, col);
                Cell cell = spreadsheet.getActiveCells().get(cellCoordinate);
                if (cell != null && cell.getEffectiveValue().getValue() instanceof Double) {
                    Double value = (Double) cell.getEffectiveValue().getValue();
                    rowImpl.addCellToRow(cell, value);
                    isEmptyRow = false;
                } else {
                    rowImpl.addCellToRow(cell, null);
                }
            }

            if (!isEmptyRow) {
                rows.add(rowImpl);
            }
        }

        return rows;
    }
}

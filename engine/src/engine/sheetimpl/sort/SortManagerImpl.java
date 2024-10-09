package engine.sheetimpl.sort;

import engine.sheetimpl.cellimpl.api.Cell;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.api.Spreadsheet;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetimpl.row.RowImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class SortManagerImpl implements SortManager, Serializable {

    public SortManagerImpl() {
    }

    @Override
    public void sortRowsByColumns(List<Coordinate> rangeCoordinates, List<Character> columnIndices, Spreadsheet spreadsheet) {
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

        List<RowImpl> rows = extractRowsInRange(start, end, columnIndicesToSort, spreadsheet);

        rows.sort((row1, row2) -> {
            for (Integer columnIndex : columnIndicesToSort) {
                EffectiveValue value1 = row1.getValue(columnIndex);
                EffectiveValue value2 = row2.getValue(columnIndex);

                // Null handling
                if (value1 == null && value2 == null) {
                    continue;
                }
                if (value1 == null) {
                    return -1;
                }
                if (value2 == null) {
                    return 1;
                }

                Double extractedValue1 = value1.extractValueWithExpectation(Double.class);
                Double extractedValue2 = value2.extractValueWithExpectation(Double.class);

                if (extractedValue1 == null && extractedValue2 == null) {
                    continue;
                }
                if (extractedValue1 == null) {
                    return -1;
                }
                if (extractedValue2 == null) {
                    return 1;
                }

                int comparisonResult = extractedValue1.compareTo(extractedValue2);
                if (comparisonResult != 0) {
                    return comparisonResult;
                }
            }
            return 0;
        });

        updateActiveCellsAfterSort(start, end, rows, spreadsheet);
    }

    private int parseColumn(char column) {
        return (toUpperCase(column) - 'A' + 1);
    }

    private List<RowImpl> extractRowsInRange(Coordinate start, Coordinate end, List<Integer> columnIndicesToSort, Spreadsheet spreadsheet) {
        List<RowImpl> rows = new ArrayList<>();

        for (int row = start.row(); row <= end.row(); row++) {
            RowImpl rowImpl = new RowImpl(row);
            boolean isEmptyRow = true;

            for (int col = start.column(); col <= end.column(); col++) {
                Coordinate cellCoordinate = CoordinateFactory.createCoordinate(row, col);
                Cell cell = spreadsheet.getActiveCells().get(cellCoordinate);
                EffectiveValue effectiveValue = (cell != null) ? cell.getEffectiveValue() : null;

                rowImpl.addCellToRow(cell, effectiveValue);
                if (effectiveValue != null) {
                    isEmptyRow = false;
                }
            }

            if (!isEmptyRow) {
                rows.add(rowImpl);
            }
        }

        return rows;
    }

    private void updateActiveCellsAfterSort(Coordinate start, Coordinate end, List<RowImpl> sortedRows, Spreadsheet spreadsheet) {
        int rowIndex = start.row();

        for (RowImpl row : sortedRows) {
            for (int col = start.column(); col <= end.column(); col++) {
                Coordinate newCoordinate = CoordinateFactory.createCoordinate(rowIndex, col);
                Cell cell = row.getCell(col);

                if (cell != null) {
                    spreadsheet.getActiveCells().put(newCoordinate, cell);
                }
            }
            rowIndex++;
        }
    }
}

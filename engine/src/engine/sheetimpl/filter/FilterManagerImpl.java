package engine.sheetimpl.filter;

import engine.api.Cell;
import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.sheetimpl.SpreadsheetImpl;
import engine.sheetimpl.row.RowImpl;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class FilterManagerImpl implements FilterManager , Serializable {

    public FilterManagerImpl() {}

    @Override
    public void filterSheet(Character selectedColumn, List<Coordinate> rangesToFilter, List<String> selectedValues, SpreadsheetImpl spreadsheet) {
        Coordinate start = rangesToFilter.get(0);
        Coordinate end = rangesToFilter.get(1);

        int columnIndex = parseColumn(selectedColumn);

        if (columnIndex < start.column() || columnIndex > end.column()) {
            throw new IllegalArgumentException("Selected column " + selectedColumn + " is out of the selected cell range.");
        }

        List<RowImpl> rows = extractRowsInRange(start, end, columnIndex, spreadsheet);

        List<RowImpl> filteredRows = new ArrayList<>();
        for (RowImpl row : rows) {
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                EffectiveValue effectiveValue = cell.getEffectiveValue();
                String cellValue = effectiveValue.getValue().toString();
                if (selectedValues.contains(cellValue)) {
                    filteredRows.add(row);
                }
            }
        }
        updateActiveCellsAfterFilter(start, end, filteredRows, spreadsheet);
    }

    private int parseColumn(char column) {
        return (toUpperCase(column) - 'A' + 1);
    }

    private List<RowImpl> extractRowsInRange(Coordinate start, Coordinate end, int columnIndex, SpreadsheetImpl spreadsheet) {
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

    private void updateActiveCellsAfterFilter(Coordinate start, Coordinate end, List<RowImpl> filteredRows, SpreadsheetImpl spreadsheet) {
        int rowIndex = start.row();

        for (RowImpl row : filteredRows) {
            for (int col = start.column(); col <= end.column(); col++) {
                Coordinate newCoordinate = CoordinateFactory.createCoordinate(rowIndex, col);
                Cell cell = row.getCell(col);

                if (cell != null) {
                    spreadsheet.getActiveCells().put(newCoordinate, cell);
                } else {
                    spreadsheet.getActiveCells().remove(newCoordinate);
                }
            }
            rowIndex++;
        }

        while (rowIndex <= end.row()) {
            for (int col = start.column(); col <= end.column(); col++) {
                Coordinate newCoordinate = CoordinateFactory.createCoordinate(rowIndex, col);
                spreadsheet.getActiveCells().remove(newCoordinate);
            }
            rowIndex++;
        }
    }
}

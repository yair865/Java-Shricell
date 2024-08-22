package sheetimpl.cellimpl.coordinate;

import java.util.HashMap;
import java.util.Map;

public class CoordinateFactory {

    private static final Map<String, Coordinate> cachedCoordinates = new HashMap<>();

    public static Coordinate createCoordinate(int row, int column) {
        if (row <= 0) {
            throw new IllegalArgumentException("Row number must be positive. Provided: " + row);
        }
        if (column <= 0) {
            throw new IllegalArgumentException("Column number must be positive. Provided: " + column);
        }

        String key = row + ":" + column;
        if (cachedCoordinates.containsKey(key)) {
            return cachedCoordinates.get(key);
        }

        CoordinateImpl coordinate = new CoordinateImpl(row, column);
        cachedCoordinates.put(key, coordinate);

        return coordinate;
    }

    public static Coordinate createCoordinate(String cellId) {
        if (cellId == null || cellId.isEmpty()) {
            throw new IllegalArgumentException("Cell ID cannot be null or empty.");
        }

        String columnPart = cellId.substring(0, 1);
        String rowPart = cellId.substring(1);

        if (!columnPart.matches("[A-Za-z]")) {
            throw new IllegalArgumentException("Invalid column letter in cell ID: " + cellId);
        }
        if (!rowPart.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid row number in cell ID: " + cellId);
        }

        int column = convertColumnLetterToNumber(columnPart);
        int row = Integer.parseInt(rowPart);

        return createCoordinate(row, column);
    }

    public static int convertColumnLetterToNumber(String columnLetter) {
        return columnLetter.charAt(0) - 'A' + 1;
    }
}



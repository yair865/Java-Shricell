package sheetimpl.cellimpl.coordinate;

import java.util.HashMap;
import java.util.Map;

public class CoordinateFactory {

    private static final Map<String, Coordinate> cachedCoordinates = new HashMap<>();

    public static Coordinate createCoordinate(int row, int column) {
        String key = row + ":" + column;
        if (cachedCoordinates.containsKey(key)) {
            return cachedCoordinates.get(key);
        }

        CoordinateImpl coordinate = new CoordinateImpl(row, column);
        cachedCoordinates.put(key, coordinate);

        return coordinate;
    }

    public static Coordinate createCoordinate(String cellId) {
        int column = convertColumnLetterToNumber(cellId.substring(0, 1));
        int row = Integer.parseInt(cellId.substring(1));

        return createCoordinate(row, column);
    }

    public static int convertColumnLetterToNumber(String columnLetter) {
        return columnLetter.charAt(0) - 'A' + 1;
    }
}



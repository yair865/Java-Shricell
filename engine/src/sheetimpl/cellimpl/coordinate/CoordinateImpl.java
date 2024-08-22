package sheetimpl.cellimpl.coordinate;

public record CoordinateImpl(int row, int column) implements Coordinate {

    @Override
    public String toString() {
        char columnLetter = (char) ('A' + (column - 1));
        return columnLetter + Integer.toString(row);
    }
}


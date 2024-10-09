package engine.sheetimpl.cellimpl.coordinate;

import java.io.Serializable;

public record CoordinateImpl(int row, int column) implements Coordinate, Serializable {

    @Override
    public String toString() {
        char columnLetter = (char) ('A' + (column - 1));
        return columnLetter + Integer.toString(row);
    }
}


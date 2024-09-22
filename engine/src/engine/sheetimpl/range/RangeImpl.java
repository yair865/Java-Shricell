package engine.sheetimpl.range;

import engine.api.Coordinate;
import engine.api.Range;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.util.ArrayList;
import java.util.List;

public class RangeImpl implements Range {
    private Coordinate start;
    private Coordinate end;
    private List<Coordinate> cellsInRange;

    public RangeImpl(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        this.initRange();
    }

    @Override
    public void initRange() {
        cellsInRange = new ArrayList<>();

        int startRow = start.row();
        int endRow = end.row();
        int startCol = start.column();
        int endCol = end.column();

        if (startRow > endRow || startCol > endCol) {
            throw new IllegalArgumentException("Invalid range: start coordinate must be less than or equal to end coordinate.");
        }

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                Coordinate coordinate = CoordinateFactory.createCoordinate(row, col);
                cellsInRange.add(coordinate);
            }
        }
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return cellsInRange;
    }

    @Override
    public Coordinate getStart() {
        return start;
    }

    @Override
    public Coordinate getEnd() {
        return end;
    }
}

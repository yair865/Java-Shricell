package engine.sheetimpl.range;

import engine.sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.List;

public interface Range {
    void initRange();

    List<Coordinate> getCoordinates();

    Coordinate getStart();

    Coordinate getEnd();
}

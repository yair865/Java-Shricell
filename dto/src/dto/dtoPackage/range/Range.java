package dto.dtoPackage.range;

import dto.dtoPackage.coordinate.Coordinate;

import java.util.List;

public interface Range {

    List<Coordinate> getCoordinates();

    Coordinate getStart();

    Coordinate getEnd();
}

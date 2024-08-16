package dtoPackage;

import sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.List;
import java.util.Map;

public record SpreadsheetDTO(String name, int version, Map<Coordinate , CellDTO> cells) {}
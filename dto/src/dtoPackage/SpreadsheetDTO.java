package dtoPackage;

import java.util.List;

public record SpreadsheetDTO(String name, int version, List<List<CellDTO>> cells) {}
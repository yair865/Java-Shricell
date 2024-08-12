package dtoPackage;

import java.util.List;

public record CellDTO(String cellId, String originalValue, String effectiveValue, int lastModifiedVersion, List<String> dependentCells, List<String> influencingCells) {}

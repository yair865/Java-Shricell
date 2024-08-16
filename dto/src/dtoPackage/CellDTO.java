package dtoPackage;

import api.Cell;
import api.EffectiveValue;

import java.util.List;

public record CellDTO(String originalValue, EffectiveValue effectiveValue, int lastModifiedVersion, List<Cell> dependentCells, List<Cell> influencingCells) {}

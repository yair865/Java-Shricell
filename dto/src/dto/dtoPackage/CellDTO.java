package dto.dtoPackage;

import engine.api.Coordinate;
import engine.api.EffectiveValue;

public record CellDTO(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue, int lastModifiedVersion) {}

package dto.dtoPackage;

import engine.api.EffectiveValue;

public record CellDTO(String originalValue, EffectiveValue effectiveValue, int lastModifiedVersion) {}

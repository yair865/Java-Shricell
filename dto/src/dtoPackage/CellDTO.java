package dtoPackage;

import api.EffectiveValue;

public record CellDTO(String originalValue, EffectiveValue effectiveValue, int lastModifiedVersion) {}

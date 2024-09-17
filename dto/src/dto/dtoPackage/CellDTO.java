package dto.dtoPackage;

import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.sheetimpl.cellimpl.CellStyle;

public record CellDTO(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue,
                      int lastModifiedVersion , CellStyle cellStyle) {}


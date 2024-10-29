package dto.dtoPackage;

import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.CellStyle;
import engine.sheetimpl.utils.CellType;

public record CellDTO(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue,
                      int lastModifiedVersion , CellStyle cellStyle , boolean containsFunction ,
                      CellType cellType, String modifiedBy) {}


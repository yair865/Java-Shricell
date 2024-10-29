package dto.dtoPackage;

import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.cellimpl.CellStyle;

public record CellDTO(Coordinate coordinate, String originalValue, EffectiveValue effectiveValue,
                      int lastModifiedVersion , CellStyle cellStyle , boolean containsFunction ,
                      CellType cellType, String modifiedBy) {}


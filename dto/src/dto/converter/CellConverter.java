package dto.converter;

import dto.dtoPackage.CellDTO;
import engine.api.CellReadActions;
import engine.api.Coordinate;


public class CellConverter {

    public static CellDTO convertCellToDTO(Coordinate coordinate, CellReadActions cell) {
        return new CellDTO(
                coordinate,
                cell.getOriginalValue(),
                cell.getEffectiveValue(),
                cell.getLastModifiedVersionVersion()
        );
    }
}

package dto.converter;

import engine.api.CellReadActions;
import dto.dtoPackage.CellDTO;

public class CellConverter {

    public static CellDTO convertCellToDTO(CellReadActions cell) {
        return new CellDTO(
                cell.getOriginalValue(),
                cell.getEffectiveValue(),
                cell.getLastModifiedVersionVersion()
        );
    }
}

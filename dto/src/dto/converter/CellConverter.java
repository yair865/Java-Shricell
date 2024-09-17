package dto.converter;

import dto.dtoPackage.CellDTO;
import engine.api.CellReadActions;


public class CellConverter {

    public static CellDTO convertCellToDTO(CellReadActions cell) {
        return new CellDTO(
                cell.getCoordinate(),
                cell.getOriginalValue(),
                cell.getEffectiveValue(),
                cell.getLastModifiedVersionVersion(),
                cell.getCellStyle()
        );
    }
}

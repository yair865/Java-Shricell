package converter;

import api.Cell;
import api.CellReadActions;
import dtoPackage.CellDTO;

public class CellConverter {

    public static CellDTO convertCellToDTO(CellReadActions cell) {
        return new CellDTO(
                cell.getOriginalValue(),
                cell.getEffectiveValue(),
                cell.getLastModifiedVersionVersion()
        );
    }
}

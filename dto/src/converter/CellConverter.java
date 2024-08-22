package converter;

import api.Cell;
import dtoPackage.CellDTO;

public class CellConverter {

    public static CellDTO convertCellToDTO(Cell cell) {
        return new CellDTO(
                cell.getOriginalValue(),
                cell.getEffectiveValue(),
                cell.getLastModifiedVersionVersion()
        );
    }
}

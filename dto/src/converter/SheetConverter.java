package converter;

import api.Cell;
import api.Spreadsheet;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class SheetConverter {

    public static SpreadsheetDTO convertSheetToDTO(Spreadsheet sheetToConvert) {
        return new SpreadsheetDTO(sheetToConvert.getSheetName(),
                sheetToConvert.getVersion(),
                convertSheetCellsToDto(sheetToConvert.getActiveCells()),
                sheetToConvert.getRows(),
                sheetToConvert.getColumns(),
                sheetToConvert.getRowHeightUnits(),
                sheetToConvert.getColumnWidthUnits());
    }

    public static Map<Coordinate, CellDTO> convertSheetCellsToDto(Map<Coordinate, Cell> cellsToConvert) {
        Map<Coordinate, CellDTO> cellsDtoMap = new HashMap<>();

        for (Map.Entry<Coordinate, Cell> entry : cellsToConvert.entrySet()) {
            Coordinate coordinate = entry.getKey();
            Cell cell = entry.getValue();

            CellDTO cellDTO = CellConverter.convertCellToDTO(cell);
            cellsDtoMap.put(coordinate, cellDTO);
        }

        return cellsDtoMap;
    }


}

package dto.converter;

import component.sheetview.body.BasicCellData;

import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import dto.dtoPackage.coordinate.CoordinateFactory;
import dto.dtoPackage.CellType;

import java.util.Map;

public class CellDataProviderConverter {

    public static void convertDTOToCellData(Map<Coordinate, BasicCellData> cellDataMap, SpreadsheetDTO spreadsheetDTO) {
        int totalRows = spreadsheetDTO.rows();
        int totalColumns = spreadsheetDTO.columns();

        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= totalColumns; col++) {
                Coordinate coordinate = CoordinateFactory.createCoordinate(row, col);

                cellDataMap.computeIfAbsent(coordinate, coord -> {
                    CellDTO cellDTO = spreadsheetDTO.cells().get(coord);
                    if (cellDTO != null) {
                        return new BasicCellData(
                                formatEffectiveValue(cellDTO.effectiveValue()),
                                cellDTO.originalValue(),
                                coord.toString(),
                                cellDTO.cellStyle().getTextColor(),
                                cellDTO.cellStyle().getBackgroundColor(),
                                cellDTO.containsFunction(),
                                cellDTO.cellType(),
                                cellDTO.modifiedBy()
                        );
                    } else {
                        return new BasicCellData("", "", coord.toString(),null,null , false , CellType.EMPTY,"");
                    }
                });
            }
        }
    }

    private static String formatEffectiveValue(EffectiveValue effectiveValue) {
        if (effectiveValue.getCellType() == CellType.NUMERIC && effectiveValue.getValue() instanceof Double numericValue) {

            if (numericValue % 1 == 0) {

                return String.format("%,d", numericValue.longValue());
            } else {

                return String.format("%,.2f", numericValue);
            }
        } else if (effectiveValue.getCellType() == CellType.BOOLEAN && effectiveValue.getValue() instanceof Boolean booleanValue) {

            return booleanValue ? "TRUE" : "FALSE";

        } else return effectiveValue.getValue().toString();
    }

}

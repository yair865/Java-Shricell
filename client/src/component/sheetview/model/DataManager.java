package component.sheetview.model;

import component.sheetview.body.BasicCellData;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateFactory;
import dto.dtoPackage.CellType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager implements CellDataProvider {

    private Map<Coordinate, BasicCellData> cellDataMap;

    public DataManager() {

        this.cellDataMap = new HashMap<>();
    }
    @Override
    public BasicCellData getCellData(Coordinate coordinate) {
        return cellDataMap.get(coordinate);
    }

    public void updateCellDataMap(List<CellDTO> changedCells) {
        for (CellDTO cellInfo : changedCells) {
            Coordinate coordinate = cellInfo.coordinate(); // Get the coordinate from CellDTO
            BasicCellData cellData = cellDataMap.get(coordinate);

            if (cellData != null) {
                cellData.setEffectiveValue(formatEffectiveValue(cellInfo.effectiveValue()));
                cellData.setOriginalValue(cellInfo.originalValue());
                cellData.setLastModifiedVersion(cellInfo.lastModifiedVersion());
                cellData.setModifiedBy(cellInfo.modifiedBy());
            }
        }
    }

    public List<String> getEffectiveValuesForColumn(char column , int numberOfRows) {
        List<String> effectiveValues = new ArrayList<>();

        for (int row = 1; row <= numberOfRows; row++) {
            Coordinate coordinate = CoordinateFactory.createCoordinate(row ,column - 'A' + 1);
            BasicCellData cellData = getCellData(coordinate);

            if (cellData != null && !(cellData.getEffectiveValue().isEmpty())) {
                effectiveValues.add(cellData.getEffectiveValue());
            }
        }
        return effectiveValues;
    }


    public Map<Coordinate, BasicCellData> getCellDataMap() {
        return cellDataMap;
    }

    public static String formatEffectiveValue(EffectiveValue effectiveValue) {
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

package application.model;

import application.body.BasicCellData;

import dto.dtoPackage.CellDTO;
import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.engineimpl.Engine;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetimpl.utils.CellType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager implements CellDataProvider {

    private Map<Coordinate, BasicCellData> cellDataMap;
    private Engine engine;

    public DataManager(Engine engine) {
        this.engine = engine;
        this.cellDataMap = new HashMap<>();
    }
    @Override
    public BasicCellData getCellData(Coordinate coordinate) {
        return cellDataMap.get(coordinate);
    }

    public void updateCellDataMap(List<Coordinate> changedCells) {
        for (Coordinate coordinate : changedCells) {
            BasicCellData cellData = cellDataMap.get(coordinate);
            if (cellData != null) {
                CellDTO cellInfo = engine.getCellInfo(coordinate.toString());
                cellData.setEffectiveValue(formatEffectiveValue(cellInfo.effectiveValue()));
                cellData.setOriginalValue(cellInfo.originalValue());
                cellData.setLastModifiedVersion(cellInfo.lastModifiedVersion());
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

    private String formatEffectiveValue(EffectiveValue effectiveValue) {
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

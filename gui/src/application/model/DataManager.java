package application.model;

import application.body.BasicCellData;
import dto.dtoPackage.CellDTO;
import engine.api.Coordinate;
import engine.api.Engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private Map<Coordinate, BasicCellData> cellDataMap;
    private Engine engine;

    public DataManager(Engine engine) {
        this.cellDataMap = new HashMap<>();
        this.engine = engine;
    }

    public Map<Coordinate, BasicCellData> getCellDataMap() {
        return cellDataMap;
    }

    public void updateCellDataMap(List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            // Fetch existing cell data
            BasicCellData cellData = cellDataMap.get(coordinate);

            if (cellData != null) {
                // Fetch updated data from the engine
                CellDTO cellInfo = engine.getCellInfo(coordinate.toString()); // Assuming you pass the coordinate or ID to get the cell info
                cellData.setEffectiveValue(cellInfo.effectiveValue().toString());
                cellData.setOriginalValue(cellInfo.originalValue());
                cellData.setLastModifiedVersion(cellInfo.lastModifiedVersion());
            }
        }
    }
}


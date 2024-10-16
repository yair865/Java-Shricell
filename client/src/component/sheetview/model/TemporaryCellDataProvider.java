package component.sheetview.model;

import component.sheetview.body.BasicCellData;
import dto.converter.CellDataProviderConverter;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;

import java.util.HashMap;
import java.util.Map;

public class TemporaryCellDataProvider implements CellDataProvider {

    private Map<Coordinate, BasicCellData> temporaryCellDataMap;
    private CellDataProviderConverter converter;

    public TemporaryCellDataProvider() {
        this.temporaryCellDataMap = new HashMap<>();
        this.converter = new CellDataProviderConverter();  // Use the DTO converter
    }

    @Override
    public BasicCellData getCellData(Coordinate coordinate) {
        return temporaryCellDataMap.getOrDefault(coordinate, new BasicCellData("", "",
                coordinate.toString(),null , null));
    }

    public Map<Coordinate, BasicCellData> getTemporaryCellDataMap() {
        return temporaryCellDataMap;
    }
}

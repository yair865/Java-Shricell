package engine.sheetimpl.api;

import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.generated.STLSheet;

import java.util.List;

public interface SheetWriteActions {

    void setTitle(String sheetName);
    void setCell(Coordinate coordinate, String value,String userName);
    void setRows(int rows);
    void setColumns(int columns);
    void setRowHeightUnits(int rowHeightUnits);
    void setColumnWidthUnits(int columnWidthUnits);
    void setSheetVersion(int sheetVersion);
    void init(STLSheet loadedSheetFromXML, String userName);
    void setBackgroundColor(String cellId, String backGroundColor);
    void setTextColor(String cellId,String textColor);
    void addRange(String name, String rangeDefinition);
    void removeRange(String name);
    void filter(Character selectedColumn, String filterArea, List<String> selectedValues);
}

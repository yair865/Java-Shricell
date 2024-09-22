package engine.api;

import engine.generated.STLSheet;

public interface SheetWriteActions {

    void setTitle(String sheetName);
    void setCell(Coordinate coordinate, String value);
    void setRows(int rows);
    void setColumns(int columns);
    void setRowHeightUnits(int rowHeightUnits);
    void setColumnWidthUnits(int columnWidthUnits);
    void setSheetVersion(int sheetVersion);
    void init(STLSheet loadedSheetFromXML);
    void setBackgroundColor(String cellId, String backGroundColor);
    void setTextColor(String cellId,String textColor);
}

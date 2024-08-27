package api;

import generated.STLSheet;

public interface SheetWriteActions {

    void setTitle(String sheetName);
    void setCell(Coordinate coordinate, String value);
    void setRows(int rows);
    void setColumns(int columns);
    void setRowHeightUnits(int rowHeightUnits);
    void setColumnWidthUnits(int columnWidthUnits);
    void setSheetVersion(int sheetVersion);
    void init(STLSheet loadedSheetFromXML);
}

package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;

public interface Engine {

    void loadSpreadsheet(String filePath);
    SpreadsheetDTO getSpreadsheetState();
    CellDTO getCellInfo(String cellId);
    void updateCell(String cellId, String newValue);
    void exit();
    int getCurrentVersion();

    //List<Version> getVersionHistory();
}

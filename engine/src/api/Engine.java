package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;

import java.util.Map;

public interface Engine {

    void loadSpreadsheet(String filePath) throws Exception;

    SpreadsheetDTO getSpreadsheetState();

    CellDTO getCellInfo(String cellId);

    void updateCell(String cellId, String newValue);

    void exitProgram();

    int getCurrentVersion();

    Map<Integer, SpreadsheetDTO> getSpreadSheetByVersion();
}

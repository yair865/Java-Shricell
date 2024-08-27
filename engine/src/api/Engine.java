package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;

import java.io.IOException;
import java.util.Map;

public interface Engine {

    void loadSpreadsheet(String filePath) throws Exception;

    SpreadsheetDTO getSpreadsheetState();

    SpreadsheetDTO pokeCellAndReturnSheet(String cellId);

    CellDTO getCellInfo(String cellId);

    void updateCell(String cellId, String newValue);

    void exitProgram();

    void loadSystemFromFile(String fileName) throws IOException, ClassNotFoundException;

   void saveSystemToFile(String fileName) throws IOException;

    Map<Integer, SpreadsheetDTO> getSpreadSheetVersionHistory();

}

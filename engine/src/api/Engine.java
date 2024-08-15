package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import jakarta.xml.bind.JAXBException;

public interface Engine {

    void loadSpreadsheet(String filePath) throws Exception;
    SpreadsheetDTO getSpreadsheetState();
    CellDTO getCellInfo(String cellId);
    void updateCell(String cellId, String newValue);
    void exit();
    int getCurrentVersion();

    //List<Version> getVersionHistory();
}

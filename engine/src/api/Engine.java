package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import generated.STLSheet;
import jakarta.xml.bind.JAXBException;
import sheetimpl.cellimpl.coordinate.Coordinate;

public interface Engine {

    void loadSpreadsheet(String filePath) throws Exception;

    void convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML);

    void validateSTLSheet(STLSheet loadedSheetFromXML);

    void validateSheetLimits(int rows, int columns);

    void validateAllCellsInBound(STLSheet loadedSheetFromXML);

    void validateXmlFile(String filePath) throws Exception;

    public STLSheet loadSheetFromXmlFile(String filePath);

    void createSpreadsheetDTO();


    SpreadsheetDTO getSpreadsheetState();
    CellDTO getCellInfo(String cellId);
    void updateCell(String cellId, String newValue);
    void exit();
    int getCurrentVersion();

    //List<Version> getVersionHistory();
}

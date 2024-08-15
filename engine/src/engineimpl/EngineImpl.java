package engineimpl;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import api.Engine;
import generated.STLCell;
import generated.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import sheetimpl.SpreadsheetImpl;
import java.io.File;
import java.util.ArrayList;

public class EngineImpl implements Engine {

    private SpreadsheetImpl currentSpreadsheet;
    private SpreadsheetDTO spreadsheetDTO;


    @Override
    public void loadSpreadsheet(String filePath) throws Exception { //throws what ?
            validateXmlFile(filePath);
            STLSheet loadedSheetFromXML = loadXmlFile(filePath);
            validateSTLSheet(loadedSheetFromXML);
            convertSTLSheet2SpreadSheet(loadedSheetFromXML);
    }

    private void convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
    // TODO
    }

    private void validateSTLSheet(STLSheet loadedSheetFromXML) {
        int rows = loadedSheetFromXML.getSTLLayout().getRows();
        int columns = loadedSheetFromXML.getSTLLayout().getColumns();
        /* 4.התאים המגדירים שימוש בפונקציות מכווינים לתאים המכילים מידע שמתאים לארגומנטים של הפונקציה */

        validateSheetLimits(rows,columns);
        validateAllCellsInBound(loadedSheetFromXML);

    }

    private void validateSheetLimits(int rows, int columns) {
        if (rows < 1 || rows > 50) {
            throw new IllegalArgumentException("Invalid number of rows: " + rows + ". Rows must be between 1 and 50.");
        }

        if (columns < 1 || columns > 20) {
            throw new IllegalArgumentException("Invalid number of columns: " + columns + ". Columns must be between 1 and 20.");
        }
    }

    private void validateAllCellsInBound(STLSheet loadedSheetFromXML) {
        int maxRows = loadedSheetFromXML.getSTLLayout().getRows();
        int maxColumns = loadedSheetFromXML.getSTLLayout().getColumns();

        for (STLCell cell : loadedSheetFromXML.getSTLCells().getSTLCell()) {
            int cellRow = cell.getRow();
            int cellColumn = convertColumnLetterToNumber(cell.getColumn());

            if (cellRow < 1 || cellRow > maxRows || cellColumn < 1 || cellColumn > maxColumns) {
                throw new IllegalArgumentException("Cell at position (" + cellRow + ", " + cellColumn +
                        ") is outside the sheet boundaries: max rows = " + maxRows +
                        ", max columns = " + maxColumns);
            }
        }
    }

    private static void validateXmlFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found.");
        }
        if (!filePath.endsWith(".xml")) {
            throw new Exception("File is not an XML file.");
        }
    }

    private static STLSheet loadXmlFile(String filePath) throws JAXBException {
        File file = new File(filePath);
        JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (STLSheet) jaxbUnmarshaller.unmarshal(file);
    }


    @Override
    public SpreadsheetDTO getSpreadsheetState() {
        return this.spreadsheetDTO;
    }

    @Override
    public CellDTO getCellInfo(String cellId) {
        // Logic to get cell information
        // For simplicity, returning a mock CellDTO
        return new CellDTO(cellId, "originalValue", "effectiveValue", 1, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        // Logic to update a cell value
        // Example: update the cell in spreadsheetDTO
        // For simplicity, no actual update in the mock
    }

    @Override
    public void exit() {

    }

    @Override
    public int getCurrentVersion() {
        return 0;
    }

 /*   @Override
    public List<Version> getVersionHistory() {
        // Logic to get version history
        // Example: returning mock version history
        return Arrays.asList(new Version(1, 5), new Version(2, 3));
    }*/

    public static int convertColumnLetterToNumber(String columnAsLetter) {
        return columnAsLetter.charAt(0) - 'A' + 1;
    }
}

package engineimpl;

import api.Cell;
import api.Engine;
import api.Spreadsheet;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import generated.STLCell;
import generated.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import sheetimpl.SpreadsheetImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.io.File;
import java.lang.reflect.InaccessibleObjectException;
import java.util.HashMap;
import java.util.List;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.convertColumnLetterToNumber;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;


public class EngineImpl implements Engine {

public static final int MAX_ROWS = 50;
    public static final int MAX_COLUMNS = 20;
/*    public static void main(String[] args) {
        Engine engine = new EngineImpl();

        try {
            engine.loadSpreadsheet("D:\\Users\\yair8\\Downloads\\basic.xml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SpreadsheetDTO sheet = engine.getSpreadsheetState();
        Map<Coordinate, CellDTO> cells = sheet.cells();

        System.out.println(sheet.name());
        System.out.println(sheet.rows());
        System.out.println(sheet.columns());
        System.out.println(sheet.rowHeightUnits());
        System.out.println(sheet.columnWidthUnits());

        for(Map.Entry<Coordinate, CellDTO> entry : cells.entrySet()) {
            System.out.println("\t" + entry.getKey());
            System.out.println("\t" + entry.getValue().originalValue());
            System.out.println("\t" + entry.getValue().effectiveValue());
        }
    }*/

   // private Map<Integer , SpreadsheetDTO> spreadsheetsByVersions;
    private SpreadsheetImpl currentSpreadsheet;

    @Override
    public void loadSpreadsheet(String filePath) throws Exception {
            validateXmlFile(filePath);
            STLSheet loadedSheetFromXML = loadSheetFromXmlFile(filePath);
            validateSTLSheet(loadedSheetFromXML);
            Spreadsheet spreadsheetConverted = convertSTLSheet2SpreadSheet(loadedSheetFromXML);
            spreadsheetConverted.validateRefExpressions();  //TODO
            this.currentSpreadsheet = spreadsheetConverted;
    }

    private Spreadsheet convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
        Spreadsheet spreadsheet = new SpreadsheetImpl();
        spreadsheet.init(loadedSheetFromXML);


        return spreadsheet;
    }


    private void validateSTLSheet(STLSheet loadedSheetFromXML) {
        int rows = loadedSheetFromXML.getSTLLayout().getRows();
        int columns = loadedSheetFromXML.getSTLLayout().getColumns();
        validateSheetLimits(rows,columns);
    }

    private void validateSheetLimits(int rows, int columns) {
        if (rows < 1 || rows > MAX_ROWS) {
            throw new IllegalArgumentException("Invalid number of rows: " + rows + ". Rows must be between 1 and 50.");
        }

        if (columns < 1 || columns > MAX_COLUMNS) {
            throw new IllegalArgumentException("Invalid number of columns: " + columns + ". Columns must be between 1 and 20.");
        }
    }


    private void validateXmlFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found.");
        }
        if (!filePath.endsWith(".xml")) {
            throw new Exception(file.getName() +" is not an XML file.\n");
        }
    }


    private STLSheet loadSheetFromXmlFile(String filePath) {
        try {
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (STLSheet) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException(e); // consider creating special Exception to throw.
        }
    }

    @Override
    public SpreadsheetDTO getSpreadsheetState() {
        validateSheetIsLoaded();
        return convertSheetToDTO(this.currentSpreadsheet);
    }

    @Override
    public CellDTO getCellInfo(String cellId) {
        validateSheetIsLoaded();
        Coordinate cellCoordinate = createCoordinate(cellId);
        Cell cellToDTO = this.currentSpreadsheet.getCell(cellCoordinate);

        return new CellDTO(cellToDTO.getOriginalValue(),
                cellToDTO.getEffectiveValue(),
                cellToDTO.getLastModifiedVersionVersion());
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        validateSheetIsLoaded();
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        this.currentSpreadsheet.setCell(coordinate,newValue);
    }

    @Override
    public void exitProgram() {
        System.exit(0);
    }

    @Override
    public int getCurrentVersion() {
        validateSheetIsLoaded();
        //TODO
        return 0;
    }

    private void validateSheetIsLoaded() {
        if (this.currentSpreadsheet == null) {
            throw new InaccessibleObjectException("File is not loaded yet.\n");
        }
    }



/*   @Override
    public List<Version> getVersionHistory() {
        // Logic to get version history
        // Example: returning mock version history
        return Arrays.asList(new Version(1, 5), new Version(2, 3));
    }*/
}

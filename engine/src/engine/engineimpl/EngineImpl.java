package engine.engineimpl;

import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.CellReadActions;
import engine.api.Coordinate;
import engine.api.Engine;
import engine.api.Spreadsheet;
import engine.generated.STLSheet;
import engine.sheetimpl.SpreadsheetImpl;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import static dto.converter.CellConverter.convertCellToDTO;
import static dto.converter.SheetConverter.convertSheetToDTO;
import static engine.sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;


public class EngineImpl implements Engine, Serializable{

    public static final int MAX_ROWS = 50;
    public static final int MAX_COLUMNS = 20;
    public static final int LOAD_VERSION = 1;

    private Map<Integer, Spreadsheet> spreadsheetsByVersions;
    int currentSpreadSheetVersion;


    @Override
    public void loadSpreadsheet(String filePath) throws Exception {
        validateXmlFile(filePath);
        STLSheet loadedSheetFromXML = loadSheetFromXmlFile(filePath);
        validateSTLSheet(loadedSheetFromXML);
        Spreadsheet loadedSpreadSheet = convertSTLSheet2SpreadSheet(loadedSheetFromXML);
        spreadsheetsByVersions = new HashMap<>();
        currentSpreadSheetVersion = LOAD_VERSION;
        loadedSpreadSheet.setSheetVersion(LOAD_VERSION);
        spreadsheetsByVersions.put(LOAD_VERSION, convertSTLSheet2SpreadSheet(loadedSheetFromXML));
    }

    private Spreadsheet convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
        Spreadsheet spreadsheet = new SpreadsheetImpl();

        try {
            spreadsheet.init(loadedSheetFromXML);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new IllegalArgumentException("In file: " + loadedSheetFromXML.getName() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return spreadsheet;
    }

    private void validateSTLSheet(STLSheet loadedSheetFromXML) {
        int rows = loadedSheetFromXML.getSTLLayout().getRows();
        int columns = loadedSheetFromXML.getSTLLayout().getColumns();
        validateSheetLimits(rows, columns);
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
            throw new Exception(file.getName() + " is not an XML file.\n");
        }
    }

    private STLSheet loadSheetFromXmlFile(String filePath) {
        try {
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (STLSheet) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load the data from the XML file."); // consider creating special Exception to throw.
        }
    }

    @Override
    public SpreadsheetDTO getSpreadsheetState() {
        validateSheetIsLoaded();
        return convertSheetToDTO(spreadsheetsByVersions.get(currentSpreadSheetVersion));
    }

    @Override
    public SpreadsheetDTO pokeCellAndReturnSheet(String cellId)
    {
        validateSheetIsLoaded();
        Coordinate cellCoordinate = createCoordinate(cellId);
        spreadsheetsByVersions.get(currentSpreadSheetVersion).getCell(cellCoordinate);
        return convertSheetToDTO(spreadsheetsByVersions.get(currentSpreadSheetVersion));
    }

    @Override
    public CellDTO getCellInfo(String cellId) {
        validateSheetIsLoaded();
        Coordinate cellCoordinate = createCoordinate(cellId);
        CellReadActions cellToDTO = spreadsheetsByVersions.get(currentSpreadSheetVersion).getCell(cellCoordinate);

        return convertCellToDTO(cellToDTO);
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        validateSheetIsLoaded();
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        Spreadsheet currentSpreadsheet = spreadsheetsByVersions.get(currentSpreadSheetVersion).copySheet();

        try {
            currentSpreadSheetVersion++;
            currentSpreadsheet.setSheetVersion(currentSpreadSheetVersion);
            spreadsheetsByVersions.put(currentSpreadSheetVersion,currentSpreadsheet);
            spreadsheetsByVersions.get(currentSpreadSheetVersion).setCell(coordinate, newValue); // consider set before put
        } catch (InvalidParameterException e) { //roll-back only.
            spreadsheetsByVersions.remove(currentSpreadSheetVersion);
            currentSpreadSheetVersion--;
        } catch (Exception e) {
            spreadsheetsByVersions.remove(currentSpreadSheetVersion);
            currentSpreadSheetVersion--;
            throw e;
        }
    }

    private void validateSheetIsLoaded() {
        if (spreadsheetsByVersions == null) {
            throw new IllegalStateException("File is not loaded yet.");
        }
        if (spreadsheetsByVersions.get(LOAD_VERSION) == null) {
            throw new IllegalStateException("File is not loaded yet.");
        }
    }


    @Override
    public Map<Integer, SpreadsheetDTO> getSpreadSheetVersionHistory() {
        validateSheetIsLoaded();
        Map<Integer, SpreadsheetDTO> spreadSheetByVersionDTO = new HashMap<>();

        for (Map.Entry<Integer, Spreadsheet> entry : spreadsheetsByVersions.entrySet()) {
            spreadSheetByVersionDTO.put(entry.getKey(), convertSheetToDTO(spreadsheetsByVersions.get(entry.getKey())));
        }

        return spreadSheetByVersionDTO;
    }


    @Override
    public void saveSystemToFile(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this); // Save the current instance of the Engine
            out.flush();
        }
    }

    @Override
    public void loadSystemFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            EngineImpl loadedEngine = (EngineImpl) in.readObject();

            this.spreadsheetsByVersions = loadedEngine.spreadsheetsByVersions;
            this.currentSpreadSheetVersion = loadedEngine.currentSpreadSheetVersion;
        }
    }

    @Override
    public void exitProgram() {
        System.exit(0);
    }


    @Override
    public SpreadsheetDTO getSpreadSheetByVersion(int version)
    {
        validateSheetIsLoaded();
        return convertSheetToDTO(spreadsheetsByVersions.get(version));
    }

    @Override
    public Integer getCurrentVersion() {
        return currentSpreadSheetVersion;
    }
}

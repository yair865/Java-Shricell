package engine.sheetmanager;

import dto.converter.SheetConverter;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.generated.STLSheet;
import engine.sheetimpl.SpreadsheetImpl;
import engine.sheetimpl.api.Spreadsheet;
import engine.sheetimpl.cellimpl.api.CellReadActions;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.versionmanager.*;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

import static dto.converter.CellConverter.convertCellToDTO;
import static engine.sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;


public class SheetManagerImpl implements SheetManager, Serializable{

    public static final int MAX_ROWS = 50;
    public static final int MAX_COLUMNS = 20;
    public static final int LOAD_VERSION = 1;

    private Spreadsheet whatIfCopy;
    private VersionManager versionManager;
    private String ownerName;

    public SheetManagerImpl(String userName) {
        this.versionManager = new VersionManagerImpl();
        this.ownerName = userName;
    }

    @Override
    public void loadSpreadsheet(String filePath) throws Exception {
        validateXmlFile(filePath);
        STLSheet loadedSheetFromXML = loadSheetFromXmlFilePath(filePath);
        validateSTLSheet(loadedSheetFromXML);
        Spreadsheet loadedSpreadSheet = convertSTLSheet2SpreadSheet(loadedSheetFromXML);
        versionManager.setVersionNumber(LOAD_VERSION);
        loadedSpreadSheet.setSheetVersion(LOAD_VERSION);
        versionManager.addVersion(LOAD_VERSION ,convertSTLSheet2SpreadSheet(loadedSheetFromXML));
    }

    @Override
    public String loadSpreadsheet(InputStream fileContent) {
        STLSheet loadedSheetFromXML = loadSheetFromXmlFile(fileContent);
        validateSTLSheet(loadedSheetFromXML);
        Spreadsheet loadedSpreadSheet = convertSTLSheet2SpreadSheet(loadedSheetFromXML);
        versionManager.setVersionNumber(LOAD_VERSION);
        loadedSpreadSheet.setSheetVersion(LOAD_VERSION);
        versionManager.addVersion(LOAD_VERSION ,convertSTLSheet2SpreadSheet(loadedSheetFromXML));

        return loadedSpreadSheet.getSheetName();
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

    private STLSheet loadSheetFromXmlFilePath(String filePath) {
        try {
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (STLSheet) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load the data from the XML file.");
        }
    }

    private STLSheet loadSheetFromXmlFile(InputStream fileContent) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (STLSheet) jaxbUnmarshaller.unmarshal(fileContent);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load the data from the XML file.");
        }
    }

    @Override
    public SpreadsheetDTO getSpreadsheetState() {
        validateSheetIsLoaded();
        return versionManager.getCurrentVersionDTO();
    }

    @Override
    public SpreadsheetDTO pokeCellAndReturnSheet(String cellId) {
        validateSheetIsLoaded();
        Coordinate cellCoordinate = createCoordinate(cellId);
        versionManager.getCurrentVersion().getCell(cellCoordinate);
        return versionManager.getCurrentVersionDTO();
    }

    @Override
    public CellDTO getCellInfo(String cellId) {
        validateSheetIsLoaded();
        Coordinate cellCoordinate = createCoordinate(cellId);
        CellReadActions cellToDTO = versionManager.getCurrentVersion().getCell(cellCoordinate);
        return convertCellToDTO(cellToDTO);
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        validateSheetIsLoaded();
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        Spreadsheet currentSpreadsheet = versionManager.getCurrentVersion().copySheet();

        try {
            int nextVersion = versionManager.getCurrentVersionNumber() + 1;
            currentSpreadsheet.setSheetVersion(nextVersion);
            versionManager.addVersion(nextVersion, currentSpreadsheet);
            versionManager.getCurrentVersion().setCell(coordinate, newValue); // consider set before addVersion
        } catch (InvalidParameterException e) { // roll-back only.
            versionManager.removeVersion(versionManager.getCurrentVersionNumber());
        } catch (Exception e) {
            versionManager.removeVersion(versionManager.getCurrentVersionNumber());
            throw e;
        }
    }

    private void validateSheetIsLoaded() {
        if (versionManager.getCurrentVersionNumber() == 0) {
            throw new IllegalStateException("File is not loaded yet.");
        }
        if (versionManager.getCurrentVersion() == null) {
            throw new IllegalStateException("File is not loaded yet.");
        }
    }

    @Override
    public Map<Integer, SpreadsheetDTO> getSpreadSheetVersionHistory() {
        validateSheetIsLoaded();
        return versionManager.getAllVersions();
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
            SheetManagerImpl loadedEngine = (SheetManagerImpl) in.readObject();

            this.versionManager = loadedEngine.versionManager;
        }
    }

    @Override
    public void exitProgram() {
        System.exit(0);
    }

    @Override
    public SpreadsheetDTO getSpreadSheetByVersion(int version) {
        validateSheetIsLoaded();
        return versionManager.getVersion(version);
    }

    @Override
    public Integer getCurrentVersion() {
        return versionManager.getCurrentVersionNumber();
    }

    @Override
    public SpreadsheetDTO getSpreadsheetByVersion(int version){
        validateSheetIsLoaded();
        return versionManager.getVersion(version);
    }

    @Override
    public void setSingleCellTextColor(String cellId, String textColor) {
        validateSheetIsLoaded();
        versionManager.getCurrentVersion().setTextColor(cellId, textColor);
    }

    @Override
    public void setSingleCellBackGroundColor(String cellId, String backGroundColor) {
        validateSheetIsLoaded();
        versionManager.getCurrentVersion().setBackgroundColor(cellId, backGroundColor);
    }

    @Override
    public void addRangeToSheet(String rangeName, String rangeDefinition) {
        validateSheetIsLoaded();
        if (versionManager.getCurrentVersion().rangeExists(rangeName)) {
            throw new IllegalArgumentException("A range with the name '" + rangeName + "' already exists.");
        }
        versionManager.getCurrentVersion().addRange(rangeName, rangeDefinition);
    }

    @Override
    public void removeRangeFromSheet(String name) {
        validateSheetIsLoaded();
        versionManager.getCurrentVersion().removeRange(name);
    }

    @Override
    public List<Coordinate> getRangeByName(final String rangeName) {
        validateSheetIsLoaded();
        return versionManager.getCurrentVersion().getRangeByName(rangeName).getCoordinates();
    }

    @Override
    public SpreadsheetDTO sort(String cellsRange, List<Character> selectedColumns) {
        validateSheetIsLoaded();
        Spreadsheet sheetToSort = versionManager.getCurrentVersion().copySheet();
        sheetToSort.sortSheet(cellsRange, selectedColumns);

        return SheetConverter.convertSheetToDTO(sheetToSort);
    }

    @Override
    public SpreadsheetDTO filterSheet(Character selectedColumn, String filterArea, List<String> selectedValues) {
        validateSheetIsLoaded();
        Spreadsheet sheetToFilter = versionManager.getCurrentVersion().copySheet();
        sheetToFilter.filter(selectedColumn, filterArea, selectedValues);

        return SheetConverter.convertSheetToDTO(sheetToFilter);
    }

    @Override
    public List<String> getUniqueValuesFromColumn(char columnNumber) {
        validateSheetIsLoaded();
        return versionManager.getCurrentVersion().getUniqueValuesFromColumn(columnNumber);
    }

    @Override
    public String getUserName() {
        return ownerName;
    }
    @Override
    public void setUserName(String userName) {
        this.ownerName = userName;
    }
    @Override
    public String getSheetTitle(){
        return versionManager.getCurrentVersion().getSheetName();
    }
    @Override
    public int getSheetNumberOfRows(){
        return versionManager.getCurrentVersion().getRows();
    }
    @Override
    public int getSheetNumberOfColumns(){
        return versionManager.getCurrentVersion().getColumns();
    }

    @Override
    public List<CellDTO> getCellsThatHaveChanged() {
        validateSheetIsLoaded();

        List<Coordinate> coordinatesThatHaveChanged = versionManager.getCurrentVersion().getCellsThatHaveChanged();

        return coordinatesThatHaveChanged.stream()
                .map(coordinate -> {
                    CellReadActions cell = versionManager.getCurrentVersion().getCell(coordinate);
                    return convertCellToDTO(cell); // Convert the cell to DTO
                })
                .collect(Collectors.toList());
    }

    @Override
    public SpreadsheetDTO getExpectedValue(Coordinate cellToCalculate, String newValueOfCell) {
        validateSheetIsLoaded();
        if (whatIfCopy == null || whatIfCopy.getVersion() != versionManager.getCurrentVersionNumber()) {
            whatIfCopy = versionManager.getCurrentVersion().copySheet();
        }
        whatIfCopy.setCell(cellToCalculate, newValueOfCell);

        return SheetConverter.convertSheetToDTO(whatIfCopy);
    }
}

package engineimpl;

import api.Cell;
import api.Engine;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import generated.STLCell;
import generated.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import sheetimpl.SpreadsheetImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static sheetimpl.SpreadsheetImpl.validateCoordinateInbound;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.convertColumnLetterToNumber;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;

public class EngineImpl implements Engine {

   // private Map<Integer , SpreadsheetDTO> spreadsheetsByVersions;
    private SpreadsheetImpl currentSpreadsheet;
    private SpreadsheetDTO spreadsheetDTO;


    @Override
    public void loadSpreadsheet(String filePath) throws Exception {
            validateXmlFile(filePath);
            STLSheet loadedSheetFromXML = loadSheetFromXmlFile(filePath);
            validateSTLSheet(loadedSheetFromXML);
            //validateRefExpressions()
            convertSTLSheet2SpreadSheet(loadedSheetFromXML);
    }
    @Override
    public void convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
        String sheetName = loadedSheetFromXML.getName();
        int rows = loadedSheetFromXML.getSTLLayout().getRows();
        int columns = loadedSheetFromXML.getSTLLayout().getColumns();
        int rowHeightUnits = loadedSheetFromXML.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int columWidthUnits = loadedSheetFromXML.getSTLLayout().getSTLSize().getColumnWidthUnits();

        this.currentSpreadsheet.setTitle(sheetName);
        this.currentSpreadsheet.setRows(rows);
        this.currentSpreadsheet.setColumns(columns);
        this.currentSpreadsheet.setRowHeightUnits(rowHeightUnits);
        this.currentSpreadsheet.setColumnWidthUnits(columWidthUnits);

        for (STLCell cell : loadedSheetFromXML.getSTLCells().getSTLCell()) {
            int cellRow = cell.getRow();
            int cellColumn = convertColumnLetterToNumber(cell.getColumn());
            String originalValue = cell.getSTLOriginalValue();
            this.currentSpreadsheet.setCell(cellRow, cellColumn, originalValue);
        }
    }
    @Override
    public void validateSTLSheet(STLSheet loadedSheetFromXML) {
        int rows = loadedSheetFromXML.getSTLLayout().getRows();
        int columns = loadedSheetFromXML.getSTLLayout().getColumns();

        validateSheetLimits(rows,columns);
        validateAllCellsInBound(loadedSheetFromXML);

    }
    @Override
    public void validateSheetLimits(int rows, int columns) {
        if (rows < 1 || rows > 50) {
            throw new IllegalArgumentException("Invalid number of rows: " + rows + ". Rows must be between 1 and 50.");
        }

        if (columns < 1 || columns > 20) {
            throw new IllegalArgumentException("Invalid number of columns: " + columns + ". Columns must be between 1 and 20.");
        }
    }
    @Override
    public void validateAllCellsInBound(STLSheet loadedSheetFromXML) {
        int maxRows = loadedSheetFromXML.getSTLLayout().getRows();
        int maxColumns = loadedSheetFromXML.getSTLLayout().getColumns();

        for (STLCell cell : loadedSheetFromXML.getSTLCells().getSTLCell()) {
            int cellRow = cell.getRow();
            int cellColumn = convertColumnLetterToNumber(cell.getColumn());
            Coordinate coordinate = createCoordinate(cellRow, cellColumn);
            validateCoordinateInbound(coordinate , maxRows, maxColumns);
            }
        }

    @Override
    public void validateXmlFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found.");
        }
        if (!filePath.endsWith(".xml")) {
            throw new Exception("File is not an XML file.");
        }
    }

    @Override
    public STLSheet loadSheetFromXmlFile(String filePath) {
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
        String spreadsheetDtoName = this.currentSpreadsheet.getSheetName();
        int spreadsheetVersion = this.currentSpreadsheet.getVersion();
        int sheetNumberOfRows = this.currentSpreadsheet.getRows();
        int sheetNumberOfColumns = this.currentSpreadsheet.getColumns();
        int sheetRowHeightUnits = this.currentSpreadsheet.getRowHeightUnits();
        int columnWidthUnits = this.currentSpreadsheet.getColumnWidthUnits();

        Map<Coordinate, CellDTO> cellsDtoMap = new HashMap<>();

        for (Map.Entry<Coordinate, Cell> entry : this.currentSpreadsheet.getActiveCells().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Cell cell = entry.getValue();

            CellDTO cellDTO = new CellDTO(
                    cell.getOriginalValue(),
                    cell.getEffectiveValue(),
                    cell.getVersion(),
                    cell.getDependsOn(),
                    cell.getInfluencingOn()
            );

            cellsDtoMap.put(coordinate, cellDTO);
        }

        return new SpreadsheetDTO(spreadsheetDtoName, spreadsheetVersion, cellsDtoMap, sheetNumberOfRows, sheetNumberOfColumns,sheetRowHeightUnits,columnWidthUnits);
    }

    @Override
    public CellDTO getCellInfo(String cellId) {

        Coordinate cellCoordinate = createCoordinate(cellId);
        Cell cellToDTO = this.currentSpreadsheet.getCell(cellCoordinate);

        return new CellDTO(cellToDTO.getOriginalValue(),cellToDTO.getEffectiveValue(),cellToDTO.getVersion(),cellToDTO.getDependsOn(),cellToDTO.getInfluencingOn());
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        // Logic to update a cell value
        // Example: update the cell in spreadsheetDTO
        // For simplicity, no actual update in the mock
    }

    @Override
    public void exitProgram() {
        System.out.println("Shticell closed");
        System.exit(0);
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
}

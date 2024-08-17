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
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.io.File;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.SpreadsheetImpl.validateCoordinateInbound;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.convertColumnLetterToNumber;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;

public class EngineImpl implements Engine {

   // private Map<Integer , SpreadsheetDTO> spreadsheetsByVersions;
    private SpreadsheetImpl currentSpreadsheet;

    @Override
    public void loadSpreadsheet(String filePath) throws Exception {
            validateXmlFile(filePath);
            STLSheet loadedSheetFromXML = loadSheetFromXmlFile(filePath);
            validateSTLSheet(loadedSheetFromXML);
            //validateRefExpressions() //TODO
            convertSTLSheet2SpreadSheet(loadedSheetFromXML);
    }
    @Override
    public void convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
        this.currentSpreadsheet.clearSpreadSheet();
        this.currentSpreadsheet.setTitle(loadedSheetFromXML.getName());
        this.currentSpreadsheet.setRows(loadedSheetFromXML.getSTLLayout().getRows());
        this.currentSpreadsheet.setColumns(loadedSheetFromXML.getSTLLayout().getColumns());
        this.currentSpreadsheet.setRowHeightUnits(loadedSheetFromXML.getSTLLayout().getSTLSize().getRowsHeightUnits());
        this.currentSpreadsheet.setColumnWidthUnits(loadedSheetFromXML.getSTLLayout().getSTLSize().getColumnWidthUnits());

        for (STLCell cell : loadedSheetFromXML.getSTLCells().getSTLCell()) {

            String originalValue = cell.getSTLOriginalValue();
            Coordinate coordinate = CoordinateFactory.createCoordinate(cell.getRow() , convertColumnLetterToNumber(cell.getColumn()));
            this.currentSpreadsheet.setCell(coordinate, originalValue);
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
        return convertSheetToDTO(this.currentSpreadsheet);
    }

    @Override
    public CellDTO getCellInfo(String cellId) {

        Coordinate cellCoordinate = createCoordinate(cellId);
        Cell cellToDTO = this.currentSpreadsheet.getCell(cellCoordinate);

        return new CellDTO(cellToDTO.getOriginalValue(),
                cellToDTO.getEffectiveValue(),
                cellToDTO.getLastModifiedVersionVersion());
    }

    @Override
    public void updateCell(String cellId, String newValue) {
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        this.currentSpreadsheet.setCell(coordinate,newValue);
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

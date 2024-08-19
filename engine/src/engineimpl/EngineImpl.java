package engineimpl;

import api.Cell;
import api.Engine;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import generated.STLCell;
import generated.STLCells;
import generated.STLSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import sheetimpl.SpreadsheetImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.io.File;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static converter.SheetConverter.convertSheetToDTO;
import static sheetimpl.SpreadsheetImpl.validateCoordinateInbound;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.convertColumnLetterToNumber;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;
import static sheetimpl.utils.ExpressionUtils.extractRefCoordinates;

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
            validateRefExpressions(loadedSheetFromXML);  //TODO
            convertSTLSheet2SpreadSheet(loadedSheetFromXML);
    }
    @Override
    public void convertSTLSheet2SpreadSheet(STLSheet loadedSheetFromXML) {
        this.currentSpreadsheet = new SpreadsheetImpl(
                loadedSheetFromXML.getName(),
                loadedSheetFromXML.getSTLLayout().getRows(),
                loadedSheetFromXML.getSTLLayout().getColumns(),
                loadedSheetFromXML.getSTLLayout().getSTLSize().getRowsHeightUnits(),
                loadedSheetFromXML.getSTLLayout().getSTLSize().getColumnWidthUnits(),
                new HashMap<Coordinate,Cell>(),
                new HashMap<Coordinate, List<Coordinate>>()
        );

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
        if (rows < 1 || rows > MAX_ROWS) {
            throw new IllegalArgumentException("Invalid number of rows: " + rows + ". Rows must be between 1 and 50.");
        }

        if (columns < 1 || columns > MAX_COLUMNS) {
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
            throw new Exception(file.getName() +" is not an XML file.\n");
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

    @Override
    public void validateSheetIsLoaded() {
        if (this.currentSpreadsheet == null) {
            throw new InaccessibleObjectException("File is not loaded yet.\n");
        }
    }

    private void validateRefExpressions(STLSheet loadedSheetFromXML) {
        Map<Coordinate, List<Coordinate>> dependenciesAdjacencyList = new HashMap<>();
        STLCells cells = loadedSheetFromXML.getSTLCells();
        List<STLCell> cellList = cells.getSTLCell();

        for (STLCell cell : cellList) {
            Coordinate cellCoordinate = CoordinateFactory.createCoordinate(cell.getRow() , convertColumnLetterToNumber(cell.getColumn()));
            List<Coordinate> coordinateList = extractRefCoordinates(cell.getSTLOriginalValue());

            for (Coordinate coordinate : coordinateList) {
                List<Coordinate> neighbors = dependenciesAdjacencyList.getOrDefault(coordinate,new ArrayList<>());

                neighbors.add(cellCoordinate);
                dependenciesAdjacencyList.put(coordinate, neighbors);
            }
        }

        //implement topoligical sort on dependenciesAdjacencyList.
    }


/*   @Override
    public List<Version> getVersionHistory() {
        // Logic to get version history
        // Example: returning mock version history
        return Arrays.asList(new Version(1, 5), new Version(2, 3));
    }*/
}

package enginePackage.implementation;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import enginePackage.interfaces.Engine;
import enginePackage.interfaces.Spreadsheet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EngineImpl implements Engine {

    private SpreadsheetImpl currentSpreadsheet;
    private SpreadsheetDTO spreadsheetDTO;

    @Override
    public void loadSpreadsheet(String filePath) {
        // Logic to load the spreadsheet from an XML file
        // For example purposes, we use mock data
        List<List<CellDTO>> cells = new ArrayList<>();
        cells.add(List.of(new CellDTO("1A", "", "", 0, new ArrayList<>(), new ArrayList<>())));
        this.spreadsheetDTO = new SpreadsheetDTO("MySpreadsheet", 1, cells);
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
}

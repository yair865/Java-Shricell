package enginePackage.implementation.physicalParts;

import enginePackage.api.Spreadsheet;
import enginePackage.implementation.physicalParts.cell.CellImpl;

import java.util.List;

public class SpreadsheetImpl implements Spreadsheet {
    private CellImpl[][] cells;
    private int numRows;
    private int numCols;
    private int spreadsheetVersion;
    private String spreadsheetName;


    @Override
    public void loadFromFile(String filePath) throws IllegalArgumentException {

    }

    @Override
    public String getCellValue(int row, int column) {
        return "";
    }

    @Override
    public void updateCellValue(int row, char column, String value) throws IllegalArgumentException {

    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public List<List<String>> getSpreadsheetData() {
        return List.of();
    }

    @Override
    public List<Integer> getVersionHistory() {
        return List.of();
    }

    @Override
    public void displaySpreadsheet() {

    }
}

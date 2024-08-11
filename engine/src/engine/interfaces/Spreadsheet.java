package engine.interfaces;

import java.util.List;

/**
 * Interface representing a spreadsheet.
 */
public interface Spreadsheet {

    /**
     * Loads a spreadsheet from an XML file.
     *
     * @param filePath The path to the XML file.
     * @throws IllegalArgumentException if the file is invalid.
     */
    void loadFromFile(String filePath) throws IllegalArgumentException;

    /**
     * Retrieves the value of a specific cell.
     *
     * @param row The row of the cell.
     * @param column The column of the cell.
     * @return The value of the cell.
     */
    String getCellValue(int row, char column);

    /**
     * Updates the value of a specific cell.
     *
     * @param row The row of the cell.
     * @param column The column of the cell.
     * @param value The new value for the cell.
     * @throws IllegalArgumentException if the value is invalid.
     */
    void updateCellValue(int row, char column, String value) throws IllegalArgumentException;

    /**
     * Retrieves the current version of the spreadsheet.
     *
     * @return The current version number.
     */
    int getVersion();

    /**
     * Retrieves the values of the entire spreadsheet.
     *
     * @return A 2D list representing the spreadsheet's values.
     */
    List<List<String>> getSpreadsheetData();

    /**
     * Retrieves the history of versions.
     *
     * @return A list of version numbers.
     */
    List<Integer> getVersionHistory();

    /**
     * Displays the current state of the spreadsheet.
     */
    void displaySpreadsheet();
}

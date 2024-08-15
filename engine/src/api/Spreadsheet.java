package api;

import java.util.List;

/**
 * Interface representing a spreadsheet.
 */
public interface Spreadsheet {

    int getVersion();
    Cell getCell(int row, int column);
    void setCell(int row, int column, String value);
}

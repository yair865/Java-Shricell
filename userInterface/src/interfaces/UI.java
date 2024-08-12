package interfaces;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;

public interface UI {

    void displayCellInfo(CellDTO cellDTO);

    void displayCellInfo(String cellId);

    void printSpreadSheet(SpreadsheetDTO SpreadsheetToPrint);

    void executeProgram();

    void displayMenu();

    void processCommand(String command);

    void displayVersions();

    void handleLoadFile();

    void handleDisplayCell();

    void handleUpdateCell();

    void displaySpreadSheet();


}

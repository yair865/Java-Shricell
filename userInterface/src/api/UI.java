package api;

import dtoPackage.SpreadsheetDTO;

public interface UI {

    void displayCellInfo(String cellId);

    void printSpreadSheet(SpreadsheetDTO SpreadsheetToPrint);

    void executeProgram();

    void displayMenu();

    void processCommand(String command);

    int getUserChoice();

    void displayVersions();

    void handleLoadFile();

    void handleDisplayCell();

    void handleUpdateCell();

    void displaySpreadSheet();

    void handleExit();
}

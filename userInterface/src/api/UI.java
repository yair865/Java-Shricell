package api;

import dtoPackage.SpreadsheetDTO;

public interface UI {

    void displayBasicCellInfo(String cellId);

    void printSpreadSheet(SpreadsheetDTO SpreadsheetToPrint);

    void executeProgram();

    void displayMenu();

    void processCommand();

    int getUserChoice();

    void displayVersions();

    void handleLoadFile();

    void handleDisplayCell();

    void displayFullCellInformation(String cellId);

    void handleUpdateCell();

    void displaySpreadSheet();

    void handleSaveSystemState();

    void handleLoadSystemState();

    void handleExit();
}

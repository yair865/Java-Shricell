package api;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;

public interface UI {

    void printSpreadSheet(SpreadsheetDTO SpreadsheetToPrint);

    void executeProgram();

    void displayMenu();

    void processCommand();

    int getUserChoice();

    void displayVersions();

    void handleLoadXMLFile();

    void handleDisplayCell();

    void displayFullCellInformation(String cellId);

    void displayBasicCellInfo(CellDTO currentCell, String cellId);

    void handleUpdateCell();

    void displaySpreadSheet();

    void handleSaveSystemState();

    void handleLoadSystemState();

    void handleExit();
}

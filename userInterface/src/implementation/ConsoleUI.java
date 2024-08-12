package implementation;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import enginePackage.interfaces.Engine;
import interfaces.UI;
import java.util.List;
import java.util.Scanner;


public class ConsoleUI implements UI {
    private Engine engine;
    private Scanner scanner;

    @Override
    public void displaySpreadSheet() {
        SpreadsheetDTO currentSpreadsheet = engine.getSpreadsheetState();

        System.out.println("The current version of the spreadsheet is: " + currentSpreadsheet.version());
        System.out.println("The title of the spreadsheet is: " + currentSpreadsheet.name());
        printSpreadSheet(currentSpreadsheet);
    }

    @Override
    public void displayCellInfo(String cellId)
    {
        CellDTO currentCell = engine.getCellInfo(cellId);

        System.out.println("Cell ID: " + cellId);
        System.out.println("Original Value: " + currentCell.originalValue());
        System.out.println("Effective Value: " + currentCell.effectiveValue());
        System.out.println("Last Modified Version: " + currentCell.lastModifiedVersion());
        System.out.println("Dependent Cells: " + currentCell.dependentCells());
        System.out.println("Influencing Cells: " + currentCell.influencingCells());
    }

    @Override
    public void printSpreadSheet(SpreadsheetDTO SpreadsheetToPrint)
    {
        int numRows = SpreadsheetToPrint.cells().size();
        int numCols = numRows > 0 ? SpreadsheetToPrint.cells().getFirst().size() : 0;

        // Print column headers
        System.out.print("   ");
        for (int col = 0; col < numCols; col++) {
            char colLetter = (char) ('A' + col);
            System.out.printf("%-4s", colLetter);
        }
        System.out.println();

        // Print rows with row numbers
        for (int row = 0; row < numRows; row++) {
            System.out.printf("%02d ", row + 1);
            List<CellDTO> rowCells = SpreadsheetToPrint.cells().get(row);
            for (CellDTO cell : rowCells) {
                String cellValue = cell.originalValue(); // or cell.effectiveValue() if preferred
                System.out.printf("| %-3s ", cellValue.isEmpty() ? " " : cellValue);
            }
            System.out.println("|");
        }
    }

    @Override
    public void executeProgram() {

        while (true) {
            displayMenu();
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    @Override
    public void displayMenu() {
        // Print menu header
        System.out.println("Shticell Menu:");
        // Print each menu option
        System.out.println("1. Load Spreadsheet from XML File");
        System.out.println("2. Display Current Spreadsheet");
        System.out.println("3. Display Single Cell Value");
        System.out.println("4. Update Single Cell Value");
        System.out.println("5. Display Version History");
        System.out.println("6. Exit");
    }
    @Override
    public void processCommand(String command) {
        switch (command) {
            case "1":
                handleLoadFile();
                break;
            case "2":
                displaySpreadSheet();
                break;
            case "3":
                handleDisplayCell();
                break;
            case "4":
                handleUpdateCell();
                break;
            case "5":
                displayVersions();
                break;
            case "6":
                engine.exit();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    @Override
    public void displayVersions() {
    }

    @Override
    public void handleLoadFile() {
        System.out.println("Enter file path:");
        String filePath = scanner.nextLine();
        try {
            engine.loadSpreadsheet(filePath);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    @Override
    public void handleDisplayCell() {
        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine();
        displayCellInfo(cellIdentifier);
    }
    @Override
    public void handleUpdateCell() {
        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine();
        System.out.println("Enter new value:");
        String newValue = scanner.nextLine();
        engine.updateCell(cellIdentifier, newValue);
    }


}

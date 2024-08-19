package implementation;

import api.Engine;
import api.UI;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import engineimpl.EngineImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;

import java.util.InputMismatchException;
import java.util.Scanner;


public class ConsoleUI implements UI {
    private Engine engine;
    private Scanner scanner;

    @Override
    public void executeProgram()
    {
        engine = new EngineImpl();
        scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            processCommand();
        }
    }

    @Override
    public void displayMenu()
    {
        System.out.println("Shticell Menu:");
        for (Menu option : Menu.values()) {
            System.out.println(option.ordinal() + 1 + ". " + option);
        }
    }

    @Override
    public void processCommand()
    {
        int choice = getUserChoice();

        while (choice < 1 || choice > Menu.values().length)
        {
            System.out.println("Invalid choice. Please enter a number between 1 and " + Menu.values().length + ".");
            choice = getUserChoice();
        }
        Menu selectedOption = Menu.values()[choice - 1];
        selectedOption.invoke(this);
    }


    @Override
    public int getUserChoice() {
        System.out.print("Enter your choice 1 - " + Menu.values().length + ": ");
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    @Override
    public void displayVersions() {
        System.out.println("Not implemented yet");
    }

    @Override
    public void handleLoadFile() {

        while(true) {
            System.out.println("Enter file path or Q button to return to the main menu:");
            String userInput = scanner.nextLine();

            try {
                if (userInput.equals("q".toUpperCase())) {
                    System.out.println("Returning to main menu");
                    break;
                }
                engine.loadSpreadsheet(userInput);
            } catch (Exception e) { //more catch types for different Exceptions?
                System.out.println("Error loading file: " + e.getMessage() + " please try to load again.");
            }
        }
    }
    @Override
    public void displaySpreadSheet() {
        try {
            SpreadsheetDTO currentSpreadsheet = engine.getSpreadsheetState();
            System.out.println("The current version of the spreadsheet is: " + currentSpreadsheet.version());
            System.out.println("The title of the spreadsheet is: " + currentSpreadsheet.name());
            printSpreadSheet(currentSpreadsheet);
        }catch (Exception e) {
            System.out.println("Could not display spreadsheet, because of an error: " + e.getMessage() + " please try again.");
        }

    }

    @Override
    public void printSpreadSheet(SpreadsheetDTO spreadsheet) {
        int numRows = spreadsheet.rows();
        int numCols = spreadsheet.columns();
        int rowHeightUnits = spreadsheet.rowHeightUnits();
        int columnWidthUnits = spreadsheet.columnWidthUnits();

        // Print column headers
        System.out.print("     "); // Initial spacing for row numbers
        for (int col = 0; col < numCols; col++) {

            char colLetter = (char) ('A' + col);
            System.out.printf("%-" + columnWidthUnits + "s" + " ", colLetter);
        }
        System.out.println();

        // Print each row
        for (int row = 0; row < numRows; row++) {
            // Print row number
            System.out.printf("%02d ", row + 1);

            for (int col = 0; col < numCols; col++) {
                // First, print an empty cell
                String cellValue = "";

                // Check if there's a corresponding cell in the map
                Coordinate coordinate = CoordinateFactory.createCoordinate(row+1, col+1);
                if (spreadsheet.cells().containsKey(coordinate)) {
                    CellDTO cell = spreadsheet.cells().get(coordinate);
                    cellValue = String.valueOf(cell.effectiveValue().extractValueWithExpectation(cell.effectiveValue().getCellType().getType()));
                }

                // Print the cell value
                System.out.printf("| %-" + (columnWidthUnits - 1) + "s", cellValue);
            }
            System.out.println("|");
        }
    }

    @Override
    public void handleDisplayCell() {
        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine();
        displayCellInfo(cellIdentifier);
    }

    @Override
    public void displayCellInfo(String cellId)
    {
        try{
        CellDTO currentCell = engine.getCellInfo(cellId);
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original Value: " + currentCell.originalValue());
        System.out.println("Effective Value: " + currentCell.effectiveValue());
        System.out.println("Last Modified Version: " + currentCell.lastModifiedVersion());

    }catch (Exception e) {
            System.out.println("Could not display cell because of an error: " + e.getMessage() + " please try again");
        }
    }

    @Override
    public void handleUpdateCell()
    {
        CellDTO currentCell;

        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine().trim();
        currentCell = engine.getCellInfo(cellIdentifier);

        System.out.println("Cell Identifier: " + cellIdentifier);
        System.out.println("Original Value: " + currentCell.originalValue());
        System.out.println("Effective Value: " + currentCell.effectiveValue());

        try {
        System.out.println("Enter new value (or leave blank to clear the cell):");
        String newValue = scanner.nextLine().trim();

            engine.updateCell(cellIdentifier,newValue);
            System.out.println("Cell updated successfully.");
            displaySpreadSheet();
        } catch (Exception e) {
            // Handle and display any errors that occur during the update
            System.out.println("An error occurred while updating the cell: " + e.getMessage() + "please try again.");
        }
    }
/*    @Override
    public void displayVersions() {
        // Step 1: Display version history
        Map<Integer, Integer> versionHistory = engine.getVersionHistory(); // TODO
        if (versionHistory == null || versionHistory.isEmpty()) {
            System.out.println("No versions available.");
            return;
        }

        System.out.println("Version History:");
        for (Map.Entry<Integer, Integer> entry : versionHistory.entrySet()) {
            System.out.printf("Version %d: %d cells changed%n", entry.getKey(), entry.getValue());
        }

        // Step 2: Allow user to view a specific version
        while (true) {
            System.out.println("Enter a version number to view or 'q' to quit:");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            try {
                int versionNumber = Integer.parseInt(input);

                if (versionHistory.containsKey(versionNumber)) {
                    // Display the state of the spreadsheet for the selected version
                    engine.loadVersion(versionNumber); // TODO
                    System.out.println("Displaying Spreadsheet for Version " + versionNumber + ":");
                    displaySpreadSheet();
                } else {
                    System.out.println("Invalid version number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'q' to quit.");
            }
        }
    }*/
    @Override
    public void handleExit() {
        engine.exitProgram();
    }
}

package implementation;

import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import enginePackage.interfaces.Engine;
import interfaces.UI;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
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
    public void executeProgram()
    {
        while (true)
        {
            displayMenu();
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    @Override
    public void displayMenu()
    {
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
    public void processCommand(String command)
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
        System.out.print("Enter your choice (1-6): ");
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    @Override
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
    public void handleUpdateCell()
    {
        CellDTO currentCell;

        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine().trim();
        currentCell = engine.getCellInfo(cellIdentifier);

        System.out.println("Cell Identifier: " + currentCell.cellId());
        System.out.println("Original Value: " + currentCell.originalValue());
        System.out.println("Effective Value: " + currentCell.effectiveValue());

        try {
        System.out.println("Enter new value (or leave blank to clear the cell):");
        String newValue = scanner.nextLine().trim();
        if(engine.valueIsValid(newValue)) // TODO
        {
            // Update the cell with the new value
            engine.updateCell(cellIdentifier,newValue);
            System.out.println("Cell updated successfully.");
            displaySpreadSheet();
        }
        else
        {
            System.out.println("Failed to update cell. The new value might be invalid.");
        }
        } catch (Exception e) {
            // Handle and display any errors that occur during the update
            System.out.println("An error occurred while updating the cell: " + e.getMessage());
        }
    }
    @Override
    public void handleExit()
    {
        engine.exit();
        System.exit(0);
    }
}

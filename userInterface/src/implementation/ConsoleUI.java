package implementation;

import api.EffectiveValue;
import api.Engine;
import api.UI;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import engineimpl.EngineImpl;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.cellimpl.coordinate.CoordinateFactory;
import sheetimpl.utils.CellType;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


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
        System.out.println();
    }

    @Override
    public void processCommand()
    {
        int choice = getUserChoice();

        while (choice < 1 || choice > Menu.values().length)
        {
            System.out.print("Invalid choice. Please enter a number between 1 and " + Menu.values().length + ".\n");
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
                System.out.println("Invalid input. Please enter a number between 1 and " + Menu.values().length + ".\n");
                scanner.nextLine();
            }
        }
    }

    @Override
    public void handleLoadFile() {

        while(true) {
            System.out.print("Enter file path or 'q/Q' button to return to the main menu:");
            String userInput = scanner.nextLine();

            try {
                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Returning to main menu.\n");
                    break;
                }
                engine.loadSpreadsheet(userInput);
                System.out.println("File successfully loaded!\n");
                break;
            } catch (Exception e) { //more catch types for different Exceptions?
                System.out.println("Error loading file: " + e.getMessage() + " please try to load again.\n");
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
            System.out.println("Could not display spreadsheet, because of an error: " + e.getMessage() + " please try again.\n");
        }

    }

    @Override
    public void printSpreadSheet(SpreadsheetDTO spreadsheet) {
        int numRows = spreadsheet.rows();
        int numCols = spreadsheet.columns();
        int columnWidthUnits = spreadsheet.columnWidthUnits();

        // Print column headers
        System.out.print("     ");
        for (int col = 0; col < numCols; col++) {
            char colLetter = (char) ('A' + col);
            System.out.printf("%-" + columnWidthUnits + "s" + "   ", colLetter);
        }
        System.out.println();

        // Print each row
        for (int row = 0; row < numRows; row++) {
            System.out.printf("%02d ", row + 1);

            for (int col = 0; col < numCols; col++) {
                String cellValue = "";

                Coordinate coordinate = CoordinateFactory.createCoordinate(row + 1, col + 1);
                if (spreadsheet.cells().containsKey(coordinate)) {
                    CellDTO cell = spreadsheet.cells().get(coordinate);
                    cellValue = formatEffectiveValue(cell.effectiveValue());
                }
                if(cellValue.length() > columnWidthUnits) {
                    cellValue = cellValue.substring(0, columnWidthUnits);
                }

                System.out.printf(" | %-" + (columnWidthUnits) + "s", cellValue);
            }
            System.out.println("|");
        }
    }

    private String formatEffectiveValue(EffectiveValue effectiveValue) {
        if (effectiveValue.getCellType() == CellType.NUMERIC && effectiveValue.getValue() instanceof Double numericValue) {

            if (numericValue % 1 == 0) {

                return String.format("%,d", numericValue.longValue());
            } else {

                return String.format("%,.2f", numericValue);
            }
        } else if (effectiveValue.getCellType() == CellType.BOOLEAN && effectiveValue.getValue() instanceof Boolean booleanValue) {

            return booleanValue ? "TRUE" : "FALSE";

        } else return effectiveValue.getValue().toString();
    }

    @Override
    public void handleDisplayCell() {
        while (true) {
            System.out.print("Enter cell identifier (e.g., A1) , or 'q/Q' button to return to the main menu:");
            String userInput = scanner.nextLine();
            try {
                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Returning to main menu.\n");
                    break;
                }
                displayFullCellInformation(userInput);
                break;

            }catch (Exception e) {
                System.out.println("Could not display cell because of an error: " + e.getMessage() + " please try again.\n");
            }
        }
    }

    @Override
    public void displayFullCellInformation(String cellId)
    {
        displayBasicCellInfo(cellId);
        SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState();
        Coordinate coordinate = CoordinateFactory.createCoordinate(cellId);
        System.out.println("The last modified version of the cell is: " + spreadsheetDTO.cells().get(coordinate).lastModifiedVersion());

        System.out.print("The dependents are: ");
        List<Coordinate> dependents = spreadsheetDTO.dependenciesAdjacencyList().get(coordinate);
        String dependentsOutput = (dependents != null && !dependents.isEmpty())
                ? dependents.stream()
                .map(Coordinate::toString)
                .collect(Collectors.joining(", "))
                : "None";
        System.out.print(dependentsOutput);

        System.out.print("\nThe references are: ");
        List<Coordinate> references = spreadsheetDTO.referencesAdjacencyList().get(coordinate);
        String referencesOutput = (references != null && !references.isEmpty())
                ? references.stream()
                .map(Coordinate::toString)
                .collect(Collectors.joining(", "))
                : "None";
        System.out.print(referencesOutput + "\n");
    }

    @Override
    public void displayBasicCellInfo(String cellId)
    {
        CellDTO currentCell = engine.getCellInfo(cellId);
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original Value: " + currentCell.originalValue());
        System.out.println("Effective Value: " + currentCell.effectiveValue());
    }

    @Override
    public void handleUpdateCell() {
        while (true) {
            System.out.print("Enter cell identifier (e.g., A1) , or 'q/Q' to return to the main menu:");
            String userInput = scanner.nextLine().trim();
            try {
                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Returning to main menu.\n");
                    break;
                }
                displayBasicCellInfo(userInput);
                System.out.print("\nEnter new value (or leave blank to clear the cell): ");
                String newValue = scanner.nextLine().trim();

                engine.updateCell(userInput, newValue);

                displaySpreadSheet();
                break;
            } catch (Exception e) {
                System.out.println("An error occurred while updating the cell: " + e.getMessage() + " please try again.\n");
            }
        }
    }

    @Override
    public void displayVersions() {
        // Step 1: Display version history
        Map<Integer, SpreadsheetDTO> versionHistory = engine.getSpreadSheetVersionHistory(); // TODO
        if (versionHistory == null || versionHistory.isEmpty()) {
            System.out.println("No versions available.");
            return;
        }

        System.out.println("Version History:");
        for (Map.Entry<Integer, SpreadsheetDTO> entry : versionHistory.entrySet()) {
            System.out.printf("Version %d: number of cells that changed %d %n", entry.getKey(), entry.getValue().numberOfModifiedCells());
        }

        while (true) {
            System.out.println("Enter a version number to view or 'q/Q' to quit:");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }

            try {
                int versionNumber = Integer.parseInt(input);

                if (versionHistory.containsKey(versionNumber)) { //maybe should be in engine
                    System.out.println("Displaying Spreadsheet for Version " + versionNumber + ":");
                    printSpreadSheet(versionHistory.get(versionNumber));
                    break;

                } else {
                    System.out.println("Invalid version number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'q' to quit.");
            }
        }
    }

    @Override
    public void handleExit() {
        engine.exitProgram();
    }
}

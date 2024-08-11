import engine.interfaces.Engine;
import engine.interfaces.Spreadsheet;

import java.util.Scanner;

public abstract class ConsoleUI implements UI {
    private Engine engine;
    private Scanner scanner;

    public ConsoleUI(Engine engine) {
        this.engine = engine;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displaySpreadSheet() {
        int version;
        String spreadSheetName;
        Spreadsheet curentSpreadSheet;

        version = engine.getCurrentVersion();
        System.out.println("Spreadsheet version is: " + version);
        spreadSheetName = engine.getSpreadSheetName();
        System.out.println("Spreadsheet name is: " + spreadSheetName);
        curentSpreadSheet = engine.getCurrentSpreadSheet();
        printSpreadSheet(curentSpreadSheet);



    }

    public static void printSpreadSheet(Spreadsheet currentSpreadsheet) {
        int numRows = currentSpreadsheet.getNumRows();
        int numCols = currentSpreadsheet.getNumCols();
        int cellWidth = currentSpreadsheet.getCellWidth();

        // הדפסת כותרות העמודות
        printColumnHeaders(numCols, cellWidth);

        // הדפסת כל שורה
        for (int row = 0; row < numRows; row++) {
            printRow(row, numCols, cellWidth, currentSpreadsheet);
        }
    }

    private static void printColumnHeaders(int numCols, int cellWidth) {
        StringBuilder header = new StringBuilder();
        for (int col = 0; col < numCols; col++) {
            if (col > 0) {
                header.append('|');
            }
            header.append(String.format("%" + cellWidth + "s", getColumnHeader(col)));
        }
        System.out.println(header.toString());
    }

    private static void printRow(int row, int numCols, int cellWidth, Spreadsheet currentSpreadsheet) {

        System.out.print(String.format("%02d", row + 1));


        for (int col = 0; col < numCols; col++) {
            if (col > 0) {
                System.out.print('|');
            }
            String cellValue = currentSpreadsheet.getCellValue(row, col); // check
            System.out.print(String.format("%" + cellWidth + "s", cellValue != null ? cellValue : ""));
        }
        System.out.println();
    }

    private static String getColumnHeader(int col) {
        StringBuilder sb = new StringBuilder();
        while (col >= 0) {
            sb.insert(0, (char) ('A' + (col % 26)));
            col = col / 26 - 1;
        }
        return sb.toString();
    }


    @Override
    public void start() {
        while (true) {
            displayMenu();
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    @Override
    public void displayMenu() {
        // Print menu options
    }
    @Override
    public void processCommand(String command) {
        switch (command) {
            case "1":
                handleLoadFile();
                break;
            case "2":
                engine.displaySpreadsheet();
                break;
            case "3":
                handleDisplayCell();
                break;
            case "4":
                handleUpdateCell();
                break;
            case "5":
                engine.displayVersions();
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
    public void handleLoadFile() {
        System.out.println("Enter file path:");
        String filePath = scanner.nextLine();
        try {
            engine.loadFile(filePath);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    @Override
    public void handleDisplayCell() {
        System.out.println("Enter cell identifier (e.g., A1):");
        String cellIdentifier = scanner.nextLine();
        engine.displayCell(cellIdentifier);
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

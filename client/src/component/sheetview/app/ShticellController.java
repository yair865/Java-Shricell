package component.sheetview.app;

import component.main.MainController;
import component.sheetview.app.load.LoadingStage;
import component.sheetview.body.BasicCellData;
import component.sheetview.body.BodyController;
import component.sheetview.header.HeaderController;
import component.sheetview.left.LeftController;
import component.sheetview.left.sort.SortController;
import component.sheetview.model.DataManager;
import component.sheetview.model.TemporaryCellDataProvider;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import engine.sheetmanager.SheetManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import static component.sheetview.model.DataManager.formatEffectiveValue;


public class ShticellController {

    int numberOfColumns;
    int numberOfRows;

    private SheetManager engine;

    private DataManager dataManager;

    @FXML
    private BorderPane applicationWindow;

    @FXML
    private GridPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private StackPane cellView;

    private BodyController bodyController;

    @FXML
    private LeftController leftComponentController;

    @FXML
    private VBox leftComponent;

    private SortController sortComponentController;

    private MainController mainController;

    public ShticellController() {
        this.dataManager = new DataManager(engine);
        this.applicationWindow = new BorderPane();
        this.bodyController = new BodyController();
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyController != null && leftComponentController != null) {
            headerComponentController.setShticellController(this);
            bodyController.setShticellController(this);
            leftComponentController.setShticellController(this);
            headerComponentController.setEngine(this.engine);
        }
    }

    private Alert createProgressAlert(ProgressBar progressBar) {
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Loading File");
        progressAlert.setHeaderText("Please wait while the file is loading.");
        progressAlert.close();
        BorderPane progressPane = new BorderPane();
        progressPane.setCenter(progressBar);
        progressAlert.getDialogPane().setContent(progressPane);

        progressAlert.setOnCloseRequest(event -> {
        });

        return progressAlert;
    }

/*    @FXML
    public void loadFile(String filePath) {
        LoadingStage loadingStage = new LoadingStage();
        loadingStage.showLoadingStage();

        Task<Void> loadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 100);

                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10);
                    updateProgress(i, 100);
                }

                engine.loadSpreadsheet(filePath);
                return null;
            }
        };

        loadTask.progressProperty().addListener((obs, oldValue, newValue) -> {
            loadingStage.updateProgress(newValue.doubleValue());
        });

        loadTask.setOnSucceeded(event -> {
            handleLoadSuccess(filePath, loadingStage);
        });

        loadTask.setOnFailed(event -> {
            handleLoadFailure(loadTask, loadingStage);
        });

        new Thread(loadTask).start();
    }

    private void handleLoadSuccess(String filePath, LoadingStage loadingStage) {
        loadingStage.closeLoadingStage();

        SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();

        updateUIWithSpreadsheetData(spreadSheet);

        System.out.println("Successfully loaded: " + filePath);
    }

    private void handleLoadFailure(Task<Void> loadTask, LoadingStage loadingStage) {
        loadingStage.closeLoadingStage();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Loading File");
        alert.setHeaderText("An error occurred while loading the file.");
        alert.setContentText("Error: " + loadTask.getException().getMessage() + "\nPlease try to load the file again.");
        alert.showAndWait();
    }*/

    public void updateUIWithSpreadsheetData(SpreadsheetDTO spreadSheet) {
        dataManager.getCellDataMap().clear();
        numberOfColumns = spreadSheet.columns();
        numberOfRows = spreadSheet.rows();
        convertDTOToCellData(dataManager.getCellDataMap(), spreadSheet);
        bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(),
                spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits(), dataManager);
        applicationWindow.setCenter(bodyController.getBody());
        leftComponentController.updateRangeList(spreadSheet.ranges().keySet());
        headerComponentController.setVersionsChoiceBox();
    }

    public void updateNewEffectiveValue(String cellId, String newValue) {
        try {
            engine.updateCell(cellId, newValue);
            List<Coordinate> cellsThatHaveChanged = engine.getSpreadsheetState().cellsThatHaveChanged();
            dataManager.updateCellDataMap(cellsThatHaveChanged);
            headerComponentController.setVersionsChoiceBox();
            headerComponentController.setCellOriginalValueLabel(dataManager.getCellData(CoordinateFactory.createCoordinate(cellId)).getOriginalValue());

        } catch (Exception e) {
            showErrorAlert("Update Error", "An error occurred while updating the cell.", e.getMessage());
        }
    }

    public void showSpreadsheetVersion(int version) {
        try {
            SpreadsheetDTO spreadsheetDTO = engine.getSpreadSheetByVersion(version); // Risky operation
            displayTempSheet("Spreadsheet Version " + version, spreadsheetDTO);
        } catch (Exception e) {
            showErrorAlert("Version Error", "An error occurred while retrieving the spreadsheet version.", e.getMessage());
        }
    }

    public void displayTempSheet(String title, SpreadsheetDTO spreadsheetDTO) {
        try {
            TemporaryCellDataProvider tempProvider = new TemporaryCellDataProvider();
            convertDTOToCellData(tempProvider.getTemporaryCellDataMap(), spreadsheetDTO);

            BodyController tempBodyController = new BodyController();
            tempBodyController.createGridPane(
                    spreadsheetDTO.rows(),
                    spreadsheetDTO.columns(),
                    spreadsheetDTO.rowHeightUnits(),
                    spreadsheetDTO.columnWidthUnits(),
                    tempProvider
            );

            Stage newStage = new Stage();
            newStage.setScene(new Scene(tempBodyController.getBody()));
            newStage.setTitle(title);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.show();
        } catch (Exception e) {
            showErrorAlert("Display Error", "An error occurred while displaying the temporary sheet.", e.getMessage());
        }
    }

    public static void convertDTOToCellData(Map<Coordinate, BasicCellData> cellDataMap, SpreadsheetDTO spreadsheetDTO) {
        int totalRows = spreadsheetDTO.rows();
        int totalColumns = spreadsheetDTO.columns();

        for (int row = 1; row <= totalRows; row++) {
            for (int col = 1; col <= totalColumns; col++) {
                Coordinate coordinate = CoordinateFactory.createCoordinate(row, col);

                cellDataMap.computeIfAbsent(coordinate, coord -> {
                    CellDTO cellDTO = spreadsheetDTO.cells().get(coord);
                    if (cellDTO != null) {
                        return new BasicCellData(
                                formatEffectiveValue(cellDTO.effectiveValue()),
                                cellDTO.originalValue(),
                                coord.toString(),
                                cellDTO.cellStyle().getTextColor(),
                                cellDTO.cellStyle().getBackgroundColor()
                        );
                    } else {
                        return new BasicCellData("", "", coord.toString(), null, null);
                    }
                });
            }
        }
    }

    public List<Coordinate> getDependents(String cellId) {
        try {
            SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState(); // Risky operation
            return spreadsheetDTO.dependenciesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
        } catch (Exception e) {
            showErrorAlert("Dependency Error", "An error occurred while retrieving dependents.", e.getMessage());
            return List.of();
        }
    }

    public List<Coordinate> getReferences(String cellId) {
        try {
            SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState();
            return spreadsheetDTO.referencesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
        } catch (Exception e) {
            showErrorAlert("Reference Error", "An error occurred while retrieving references.", e.getMessage());
            return List.of();
        }
    }

    public void sortSheet(String cellsRange, List<Character> selectedColumns) {
        try {
            SpreadsheetDTO sortedSheet = engine.sort(cellsRange, selectedColumns);
            displayTempSheet("Sorted Sheet", sortedSheet);
        } catch (Exception e) {
            showErrorAlert("Sorting Error", "An error occurred while sorting the sheet.", e.getMessage());
        }
    }

    public List<String> getEffectiveValuesForColumn(char column) {
        try {
            return engine.getUniqueValuesFromColumn(column);
        } catch (Exception e) {
            showErrorAlert("Column Retrieval Error", "An error occurred while retrieving values from the column.", e.getMessage());
            return List.of();
        }
    }

    public void applyFilter(Character selectedColumn, String filterArea, List<String> selectedValues) {
        try {
            SpreadsheetDTO filteredSheet = engine.filterSheet(selectedColumn, filterArea, selectedValues);
            displayTempSheet("Filtered Sheet", filteredSheet);
        } catch (Exception e) {
            showErrorAlert("Filtering Error", "An error occurred while applying the filter.", e.getMessage());
        }
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public HeaderController getHeaderController() {
        return headerComponentController;
    }

    public SheetManager getEngine() {
        return engine;
    }

    public BodyController getBodyController() {
        return bodyController;
    }

    public LeftController getLeftController() {
        return leftComponentController;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void changeTheme(String name){
        leftComponentController.setSkin(name);
        headerComponentController.setSkin(name);
        bodyController.setSkin(name);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}

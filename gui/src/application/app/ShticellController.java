package application.app;

import application.body.BodyController;
import application.header.HeaderController;
import application.left.LeftController;
import application.model.DataManager;
import application.model.TemporaryCellDataProvider;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.Coordinate;
import engine.api.Engine;
import engine.engineimpl.EngineImpl;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;

import static dto.converter.CellDataProviderConverter.convertDTOToCellData;

public class ShticellController {

    private Engine engine;

    private DataManager dataManager;

    @FXML
    private BorderPane applicationWindow;

    @FXML
    private ScrollPane headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private StackPane cellView;

    private BodyController bodyController;

    @FXML
    private LeftController leftComponentController;

    @FXML
    private AnchorPane leftComponent;

    public ShticellController() {
        this.engine = new EngineImpl();
        this.dataManager = new DataManager(engine); // Pass the Engine instance
        this.applicationWindow = new BorderPane();
        this.bodyController = new BodyController();
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null  && bodyController != null && leftComponentController != null) {
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

        progressAlert.getButtonTypes().clear();

        ButtonType okButtonType = new ButtonType("OK");
        progressAlert.getButtonTypes().add(okButtonType);

        BorderPane progressPane = new BorderPane();
        progressPane.setCenter(progressBar);
        progressAlert.getDialogPane().setContent(progressPane);

        progressAlert.setOnCloseRequest(event -> {
        });

        return progressAlert;
    }

    @FXML
    public void loadFile(String filePath) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);

        Alert progressAlert = createProgressAlert(progressBar);

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

        progressBar.progressProperty().bind(loadTask.progressProperty());

        loadTask.setOnSucceeded(event -> {
            dataManager.getCellDataMap().clear();
            SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();
            convertDTOToCellData(dataManager.getCellDataMap(), spreadSheet);
            bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(),
                    spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits(), dataManager);
            applicationWindow.setCenter(bodyController.getBody());
            headerComponentController.setVersionsChoiceBox();
            leftComponentController.updateRangeList(spreadSheet.ranges().keySet());

            progressAlert.close();
            System.out.println("Successfully loaded: " + filePath);
        });

        loadTask.setOnFailed(event -> {
            progressAlert.close();
            headerComponentController.clearNewValueTextField();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("An error occurred while loading the file.");
            alert.setContentText("Error: " + loadTask.getException().getMessage() + "\nPlease try to load the file again.");
            alert.showAndWait();
        });

        new Thread(loadTask).start();

        progressAlert.show();
    }

    public void updateNewEffectiveValue(String cellId, String newValue) {
        try {
            engine.updateCell(cellId, newValue);
            List<Coordinate> cellsThatHaveChanged = engine.getSpreadsheetState().cellsThatHaveChanged();
            dataManager.updateCellDataMap(cellsThatHaveChanged);
            headerComponentController.setVersionsChoiceBox();
            headerComponentController.setCellOriginalValueLabel(dataManager.getCellData(CoordinateFactory.createCoordinate(cellId)).getOriginalValue());

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Error");
            alert.setHeaderText("An error occurred while updating the cell.");
            alert.setContentText("Error: " + e.getMessage() + "\nPlease try again.");

            alert.showAndWait();
        }
    }

    public HeaderController getHeaderController() {
        return headerComponentController;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void showSpreadsheetVersion(int version) {
        try {
            SpreadsheetDTO spreadsheetDTO = engine.getSpreadSheetByVersion(version);
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
            newStage.setTitle("Spreadsheet Version " + version);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public BodyController getBodyController()
{
    return bodyController;
}

    public List<Coordinate> getDependents(String cellId) {
        SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState();
        return spreadsheetDTO.dependenciesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
    }

    public List<Coordinate> getReferences(String cellId) {
        SpreadsheetDTO spreadsheetDTO = engine.getSpreadsheetState();
        return spreadsheetDTO.referencesAdjacencyList().get(CoordinateFactory.createCoordinate(cellId));
    }

    public LeftController getLeftComponentController() {
        return this.leftComponentController;
    }
}

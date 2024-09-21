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
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    @FXML
    public void loadFile(String filePath) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);

        // Create a Task to handle file loading
        Task<Void> loadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simulate file loading by updating the progress bar
                updateProgress(0, 100);

                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10); // Simulating work
                    updateProgress(i, 100);
                }

                engine.loadSpreadsheet(filePath); // Your actual file loading logic here
                return null;
            }
        };

        // Create the alert for progress
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Loading File");
        progressAlert.setHeaderText("Please wait while the file is loading.");
        progressAlert.getDialogPane().setContent(progressBar);
        progressAlert.setOnCloseRequest(event -> loadTask.cancel());

        // Bind the progress bar to the task progress
        progressBar.progressProperty().bind(loadTask.progressProperty());

        // Run the task in a background thread
        new Thread(loadTask).start();

        // Show the progress dialog
        progressAlert.show();

        loadTask.setOnSucceeded(event -> {
            dataManager.getCellDataMap().clear();
            SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();
            convertDTOToCellData(dataManager.getCellDataMap(), spreadSheet);
            bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(),
                    spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits(), dataManager);
            applicationWindow.setCenter(bodyController.getBody());
            headerComponentController.setVersionsChoiceBox();

            progressAlert.close(); // Close the progress window
            System.out.println("Successfully loaded: " + filePath);
        });

        loadTask.setOnFailed(event -> {
            // Show an error message if the file loading fails
            progressAlert.close();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("An error occurred while loading the file.");
            alert.setContentText("Error: " + loadTask.getException().getMessage() + "\nPlease try to load the file again.");
            alert.showAndWait();
        });
    }

    public void updateNewEffectiveValue(String cellId, String newValue) {
        try {
            engine.updateCell(cellId, newValue);
            List<Coordinate> cellsThatHaveChanged = engine.getSpreadsheetState().cellsThatHaveChanged();
            dataManager.updateCellDataMap(cellsThatHaveChanged);
            headerComponentController.setVersionsChoiceBox();

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

}

package application.app;

import application.body.BasicCellData;
import application.body.BodyController;
import application.body.CellViewController;
import application.header.HeaderController;
import application.model.DataManager;
import application.model.TemporaryCellDataProvider;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.Coordinate;
import engine.api.Engine;
import engine.engineimpl.EngineImpl;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

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

    @FXML
    private CellViewController cellViewController;

    @FXML
    private BodyController bodyController;

    public ShticellController() {
        this.engine = new EngineImpl();
        this.dataManager = new DataManager(engine); // Pass the Engine instance
        this.applicationWindow = new BorderPane();
        this.bodyController = new BodyController();
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && cellViewController != null) {
            headerComponentController.setShticellController(this);
            cellViewController.setShticellController(this);
            bodyController.setShticellController(this);
            headerComponentController.setEngine(this.engine);
        }
    }

    @FXML
    public void loadFile(String filePath) {
        try {
            engine.loadSpreadsheet(filePath);
            dataManager.getCellDataMap().clear();
            SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();
            convertDTOToCellData(dataManager.getCellDataMap(),spreadSheet);
            bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(), spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits(), dataManager);
            applicationWindow.setCenter(bodyController.getBody());
            this.headerComponentController.setVersionsChoiceBox();
            System.out.println("Successfully loaded: " + filePath);
        } catch (Exception e) {
            // Create an alert to display the error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("An error occurred while loading the file.");
            alert.setContentText("Error: " + e.getMessage() + "\nPlease try to load the file again.");

            alert.showAndWait();
        }
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

            // Create a new instance of BodyController and configure it
            BodyController tempBodyController = new BodyController();
            tempBodyController.createGridPane(
                    spreadsheetDTO.rows(),
                    spreadsheetDTO.columns(),
                    spreadsheetDTO.rowHeightUnits(),
                    spreadsheetDTO.columnWidthUnits(),
                    tempProvider
            );

            // Create and show a new window with the BodyController's view
            Stage newStage = new Stage();
            newStage.setScene(new Scene(tempBodyController.getBody()));
            newStage.setTitle("Spreadsheet Version " + version);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

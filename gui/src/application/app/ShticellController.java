package application.app;

import application.body.BasicCellData;
import application.body.BodyController;
import application.body.CellViewController;
import application.header.HeaderController;
import application.model.DataManager;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.api.Cell;
import engine.api.Coordinate;
import engine.api.Engine;
import engine.engineimpl.EngineImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class ShticellController {

    private Engine engine;
    private DataManager dataManager;

    @FXML
    private BorderPane applicationWindow;

    @FXML
    private VBox headerComponent;

    @FXML
    private HeaderController headerComponentController;

    @FXML
    private StackPane cellView;

    @FXML
    private CellViewController cellViewController;

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
        }
    }

    @FXML
    public void loadFile(String filePath) {
        try {
            engine.loadSpreadsheet(filePath);
            SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();
            convertDTOtoCellView(spreadSheet);
            bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(), spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits());
            applicationWindow.setCenter(bodyController.getBody());
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
        engine.updateCell(cellId, newValue);
        List<Coordinate> cellsThatHaveChanged = engine.getSpreadsheetState().cellsThatHaveChanged();
        dataManager.updateCellDataMap(cellsThatHaveChanged);
    }

    public HeaderController getHeaderController() {
        return headerComponentController;
    }

    public Engine getEngine() {
        return this.engine;
    }

    public void convertDTOtoCellView(SpreadsheetDTO spreadsheetDTO) {
        for (Map.Entry<Coordinate, CellDTO> entry : spreadsheetDTO.cells().entrySet()) {
            Coordinate coordinate = entry.getKey();
            CellDTO cellDTO = entry.getValue();

            // Create a new BasicCellData or CellViewController instance
            BasicCellData cellData = new BasicCellData(
                    cellDTO.effectiveValue().toString(), // Bind the effective value from the DTO
                    cellDTO.originalValue(),  // Bind the original value from the DTO
                    coordinate.toString()     // Use the coordinate as the cell ID
            );

            dataManager.getCellDataMap().put(coordinate, cellData);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}

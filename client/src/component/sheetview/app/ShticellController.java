package component.sheetview.app;

import component.main.MainController;
import component.sheetview.body.BasicCellData;
import component.sheetview.body.BodyController;
import component.sheetview.header.HeaderController;
import component.sheetview.left.LeftController;
import component.sheetview.left.sort.SortController;
import component.sheetview.model.DataManager;
import component.sheetview.model.TemporaryCellDataProvider;
import dto.dtoPackage.CellDTO;
import dto.dtoPackage.SpreadsheetDTO;
import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.coordinate.CoordinateFactory;
import dto.dtoPackage.CellType;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.alert.AlertUtil;
import util.requestservice.ShitcellRequestServiceImpl;
import util.requestservice.ShticellRequestService;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static component.sheetview.model.DataManager.formatEffectiveValue;


public class ShticellController implements Closeable {

    int numberOfColumns;
    int numberOfRows;

    private IntegerProperty currentVersion = new SimpleIntegerProperty(1);

    private ShticellRequestService requestService;

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

    private final BooleanProperty isReader = new SimpleBooleanProperty();

    public ShticellController() {
        this.requestService = new ShitcellRequestServiceImpl();

        this.dataManager = new DataManager();
        this.applicationWindow = new BorderPane();
        this.bodyController = new BodyController();
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyController != null && leftComponentController != null) {
            headerComponentController.setShticellController(this);
            bodyController.setShticellController(this);
            leftComponentController.setShticellController(this);
        }
    }

    public void updateUIWithSpreadsheetData(SpreadsheetDTO spreadSheet) {
        dataManager.getCellDataMap().clear();
        numberOfColumns = spreadSheet.columns();
        numberOfRows = spreadSheet.rows();
        currentVersion.set(spreadSheet.version());
        convertDTOToCellData(dataManager.getCellDataMap(), spreadSheet);
        bodyController.createGridPane(spreadSheet.rows(), spreadSheet.columns(), spreadSheet.rowHeightUnits(), spreadSheet.columnWidthUnits(), dataManager);
        applicationWindow.setCenter(bodyController.getBody());
        leftComponentController.updateRangeList(spreadSheet.ranges().keySet());
        headerComponentController.setVersionsChoiceBox(spreadSheet.version());
    }

    public void updateNewEffectiveValue(String cellId, String newValue) {
        try {
            requestService.updateCell(cellId, newValue, currentVersion.get(), cellsThatHaveChanged -> {
                dataManager.updateCellDataMap(cellsThatHaveChanged);
                headerComponentController.setVersionsChoiceBox(currentVersion.get());
                String originalValue = dataManager.getCellData(CoordinateFactory.createCoordinate(cellId)).getOriginalValue();
                String modifiedBy = dataManager.getCellData(CoordinateFactory.createCoordinate(cellId)).getReviserName();
                currentVersion.set(currentVersion.get() + 1);
                headerComponentController.updateHeader(cellId, originalValue, currentVersion.get(), modifiedBy);

            });
        } catch (Exception e) {
            showErrorAlert("Update Error", "An error occurred while updating the cell.", e.getMessage());
        }
    }

    public void showSpreadsheetVersion(int version) {
        requestService.getSpreadSheetByVersion(version,
                spreadsheetDTO -> {
                    displayTempSheet("Spreadsheet Version " + version, spreadsheetDTO);
                }
        );
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
                                cellDTO.cellStyle().getBackgroundColor(),
                                cellDTO.containsFunction(),
                                cellDTO.cellType(),
                                cellDTO.modifiedBy()
                        );
                    } else {
                        return new BasicCellData("", "", coord.toString(), null, null , false , CellType.EMPTY,"");
                    }
                });
            }
        }
    }

    public void getDependents(String cellId, Consumer<List<Coordinate>> callback) {
        requestService.getDependents(cellId, dependents -> {
            Platform.runLater(() -> callback.accept(dependents));
        });
    }

    public void getReferences(String cellId, Consumer<List<Coordinate>> callback) {
        requestService.getReferences(cellId, references -> {
            Platform.runLater(() -> callback.accept(references));
        });
    }

    public void sortSheet(String cellsRange, List<Character> selectedColumns) {
        requestService.sort(cellsRange, selectedColumns, sortedSheet -> {
            Platform.runLater(() -> displayTempSheet("Sorted Sheet", sortedSheet));
        });
    }

    public void getEffectiveValuesForColumn(char column, Consumer<List<String>> callback) {
        requestService.getEffectiveValuesForColumn(column, values -> {
            Platform.runLater(() -> {
                callback.accept(values);
            });
        });
    }

    public void applyFilter(Character selectedColumn, String filterArea, List<String> selectedValues) {
        requestService.filterSheet(selectedColumn, filterArea, selectedValues, filteredSheet -> {
            Platform.runLater(() -> displayTempSheet("Filtered Sheet", filteredSheet));
        });
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

    public BodyController getBodyController() {
        return bodyController;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void changeTheme(String name) {
        leftComponentController.setSkin(name);
        headerComponentController.setSkin(name);
        bodyController.setSkin(name);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSingleCellBackGroundColor(String hexString) {
        String cellId = getHeaderController().getCellId();

        Runnable task = () -> {
            bodyController.updateCellBackgroundColor(cellId, hexString);
        };

        requestService.setSingleCellBackGroundColor(cellId, hexString,currentVersion.get() ,task);
    }

    public void setSingleCellTextColor(String hexString) {
        String cellId = getHeaderController().getCellId();

        Runnable task = () -> {
            bodyController.updateCellTextColor(cellId, hexString);
        };

        requestService.setSingleCellTextColor(cellId, hexString,currentVersion.get() ,task);
    }

    public void removeRangeFromSheet(String selectedRange, Consumer<String> callback) {

        requestService.removeRangeFromSheet(selectedRange,currentVersion.get() ,onSuccess -> {
            Platform.runLater(() -> callback.accept(onSuccess));
        });
    }

    public void addRangeToSheet(String rangeName, String coordinates, Consumer<String> callback) {
        requestService.addRangeToSheet(rangeName, coordinates,currentVersion.get() ,onSuccess -> {
            Platform.runLater(() -> callback.accept(onSuccess));
        });
    }

    public void getRangeByName(String rangeName, Consumer<List<Coordinate>> callback) {
        requestService.getRangeByName(rangeName, rangeCells -> {
            Platform.runLater(() -> callback.accept(rangeCells));
        });
    }

    public BooleanProperty isReaderProperty() {
        return isReader;
    }

    public BasicCellData getCurrentCell() {
        String cellId = headerComponentController.getCellId();
        if (cellId == null || cellId.isEmpty()) {
            AlertUtil.showErrorAlert("Error", "No cell found");
            return null;
        }
        return dataManager.getCellData(CoordinateFactory.createCoordinate(cellId));
    }

    public void displayExpectedValue(Number newValue) {
        Coordinate currentCellCoordinate = CoordinateFactory.createCoordinate(headerComponentController.getCellId());
        requestService.getExpectedValue(currentCellCoordinate, String.valueOf(newValue.doubleValue()), (expectedSheet) ->
                bodyController.refreshExpectedValues(expectedSheet));
    }

    public void restoreCurrentValues() {
        bodyController.restoreCurrentValues();
    }

    public BasicCellData getCell(Coordinate key) {
        return dataManager.getCellData(key);
    }

    public void getLatestVersion(Consumer<SpreadsheetDTO> callback) {
        requestService.getLatestVersion( latestVersion -> {
            Platform.runLater(() -> {
                callback.accept(latestVersion);
            });
        });
    }

    public IntegerProperty currentVersionProperty() {
        return currentVersion;
    }

    public void setActive() {
        headerComponentController.setActive();
    }

    public void backToDashboard() {
        close();
        mainController.loadDashboardPage();
    }

    @Override
    public void close()  {
        headerComponentController.close();
    }
}

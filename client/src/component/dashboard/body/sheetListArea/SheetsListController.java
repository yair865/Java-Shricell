package component.dashboard.body.sheetListArea;

import component.dashboard.DashboardController;
import component.dashboard.body.sheetListArea.sheetdata.SingleSheetData;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.Closeable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static consts.Constants.REFRESH_RATE;

public class SheetsListController implements Closeable {

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalSheets;

    @FXML
    private DashboardController dashboardController;

    @FXML
    private TableView<SingleSheetData> sheetsTableView;

    @FXML
    private TableColumn<SingleSheetData, String> userNameColumn;

    @FXML
    private TableColumn<SingleSheetData, String> sheetNameColumn;

    @FXML
    private TableColumn<SingleSheetData, String> sizeColumn;

    @FXML
    private TableColumn<SingleSheetData, String> permissionColumn;

    private StringProperty currentSheetName;

    public SheetsListController() {
        totalSheets = new SimpleIntegerProperty();
    }

    @FXML
    void initialize() {
        userNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUserName()));

        sheetNameColumn.setCellValueFactory(cellData ->
                cellData.getValue().sheetNameProperty());

        sizeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSheetSize()));

        permissionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPermission().toString()));

        currentSheetName = new SimpleStringProperty(null);

        sheetsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentSheetName.bind(newSelection.sheetNameProperty());
                refreshPermissions(newSelection.sheetNameProperty().get());
            } else {
                currentSheetName.unbind();
                currentSheetName.set(null);
            }
        });
    }

    private void refreshPermissions(String sheetName) {
        dashboardController.refreshPermissions(sheetName);
    }

    private void updateSheetsList(List<SingleSheetData> sheets) {
        ObservableList<SingleSheetData> currentItems = sheetsTableView.getItems();
        SingleSheetData selectedItem = sheetsTableView.getSelectionModel().getSelectedItem();

        if (currentItems.size() != sheets.size()) {
            Platform.runLater(() -> {
                currentItems.clear();
                currentItems.addAll(sheets);
                totalSheets.set(sheets.size());

                if (selectedItem != null && sheets.contains(selectedItem)) {
                    sheetsTableView.getSelectionModel().select(selectedItem);
                }
            });
        }
    }

    public void startListRefresher() {
        listRefresher = new SheetsListRefresher(this::updateSheetsList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @Override
    public void close() {
        sheetsTableView.getItems().clear();
        totalSheets.set(0);
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public String getCurrentSheetName() {
        return currentSheetName.get();
    }

    public StringProperty currentSheetNameProperty() {
        return currentSheetName;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}

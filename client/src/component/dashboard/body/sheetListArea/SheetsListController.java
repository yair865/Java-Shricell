package component.dashboard.body.sheetListArea;

import component.dashboard.DashboardController;
import component.dashboard.body.sheetListArea.sheetdata.SingleSheetData;
import engine.permissionmanager.PermissionType;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

        sheetsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                dashboardController.currentSheetNameProperty().bind(newSelection.sheetNameProperty());

                dashboardController.isReaderProperty().set(newSelection.getPermission() == PermissionType.READER);

                dashboardController.isNoneProperty().set(newSelection.getPermission() == PermissionType.NONE);

                refreshPermissions(newSelection.sheetNameProperty().get());
            } else {
                dashboardController.currentSheetNameProperty().unbind();
                dashboardController.isReaderProperty().set(false);
                dashboardController.isNoneProperty().set(true);
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
                    sheetsTableView.getSelectionModel().clearSelection();
                    sheetsTableView.getSelectionModel().select(selectedItem);
                }
            });
        } else {
            Platform.runLater(() -> {
                for (SingleSheetData currentItem : currentItems) {
                    SingleSheetData newItem = sheets.stream()
                            .filter(item -> item.getSheetName().equals(currentItem.getSheetName()) &&
                                    item.getUserName().equals(currentItem.getUserName()))
                            .findFirst()
                            .orElse(null);

                    if (newItem != null) {
                        String currentPermission = currentItem.getPermission().toString();
                        PermissionType newPermission = newItem.getPermission();

                        if (!currentPermission.equals(newPermission.toString())) {
                            sheetsTableView.getSelectionModel().clearSelection();
                            currentItem.setPermission(newPermission);
                            sheetsTableView.getSelectionModel().select(selectedItem);
                        }
                    }
                }
                sheetsTableView.refresh();
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

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}

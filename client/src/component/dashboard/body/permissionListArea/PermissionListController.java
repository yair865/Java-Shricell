package component.dashboard.body.permissionListArea;

import component.dashboard.DashboardController;
import component.dashboard.body.permissionListArea.permissiondata.SinglePermissionData;
import dto.dtoPackage.PermissionInfoDTO;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class PermissionListController {

    @FXML
    private TableView<SinglePermissionData> permissionTableView;

    @FXML
    private VBox permissionsListComponent;

    @FXML
    private TableColumn<SinglePermissionData, String> statusColumn;

    @FXML
    private TableColumn<SinglePermissionData, String> userColumn;

    @FXML
    private TableColumn<SinglePermissionData, String> permissionColumn;

    private ObservableList<SinglePermissionData> permissionsList;

    private DashboardController dashboardController;

    private IntegerProperty currentRequestId = new SimpleIntegerProperty();

    @FXML
    void initialize() {
        permissionsList = FXCollections.observableArrayList();

        // Set up the table columns
        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        permissionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPermission().toString()));
        statusColumn.setCellValueFactory(cellData -> {
            String status = String.valueOf(cellData.getValue().getStatus());
            return new SimpleStringProperty(status != "null" ? status : "");
        });

        permissionTableView.setItems(permissionsList);

        permissionTableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends SinglePermissionData> observable, SinglePermissionData oldValue, SinglePermissionData newValue) -> {
                    if (newValue != null) {

                        currentRequestId.set(newValue.getRequestID());
                    } else {
                        currentRequestId.set(-1);
                    }
                }
        );
    }

    public void updatePermissions(List<PermissionInfoDTO> permissionInfos) {
        // Store current items for reference
        ObservableList<SinglePermissionData> currentItems = permissionsList;

        // Clear the existing permissions
        currentItems.clear();

        // Add new permissions
        for (PermissionInfoDTO info : permissionInfos) {
            SinglePermissionData singlePermissionData = new SinglePermissionData(
                    info.username(),
                    info.permissionType(),
                    info.status(),
                    info.requestId()
            );
            currentItems.add(singlePermissionData);
        }

        Platform.runLater(() -> {
            permissionTableView.setItems(permissionsList);
        });
    }


    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public IntegerProperty currentRequestIdProperty() {
        return currentRequestId;
    }
}

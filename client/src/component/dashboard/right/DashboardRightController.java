package component.dashboard.right;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import component.dashboard.DashboardController;
import component.dashboard.right.permission.PermissionController;
import component.main.MainController;
import dto.deserialize.CoordinateTypeAdapter;
import dto.deserialize.EffectiveValueTypeAdapter;
import dto.deserialize.SpreadsheetDTODeserializer;
import dto.dtoPackage.PermissionInfoDTO;
import dto.dtoPackage.SpreadsheetDTO;
import engine.permissionmanager.request.RequestStatus;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;
import util.alert.AlertUtil;

import java.io.IOException;
import java.util.List;

import static consts.Constants.*;

public class DashboardRightController {

    private MainController mainController;

    @FXML
    private DashboardController dashboardController;

    @FXML
    private VBox dashboardRight;

    @FXML
    private Button permissionApproveButton;

    @FXML
    private Button permissionDenyButton;

    @FXML
    private Button refreshPermissionTableButton;

    @FXML
    private HBox right;

    @FXML
    private Button viewSheetButton;

    @FXML
    void permissionApproveListener(ActionEvent event) {
        sendPermissionRequest(RequestStatus.APPROVED);
    }

    private void sendPermissionRequest(RequestStatus status) {
        int requestId = dashboardController.currentRequestIdProperty().get();
        String sheetName = dashboardController.currentSheetNameProperty().get();

        if (sheetName == null || sheetName.isEmpty()) {
            AlertUtil.showErrorAlert("Sheet Selection Error", "No sheet selected.");
            return;
        }

        String url = PERMISSION_RESPONSE;

        RequestBody requestBody = new FormBody.Builder()
                .add("requestId", String.valueOf(requestId))
                .add("RequestStatus", status.name())
                .add("sheetName", sheetName)
                .build();

        HttpClientUtil.runAsyncPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Request Error", "Failed to send " + status.name().toLowerCase() + " request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> AlertUtil.showSuccessAlert(status.name() + " Successful", "Permission " + status.name().toLowerCase() + " successfully!"));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert(status.name() + " Failed", "Failed to update permission: " + response.message()));
                }
            }
        });
    }

    @FXML
    void permissionDenyButtonListener(ActionEvent event) {
        sendPermissionRequest(RequestStatus.DENIED);
    }

    @FXML
    public void refreshPermissionTableButtonListener(ActionEvent event) {
        String selectedSheetName = dashboardController.currentSheetNameProperty().get();

        if (selectedSheetName == null || selectedSheetName.isEmpty()) {
            AlertUtil.showErrorAlert("Sheet Selection Error", "No sheet selected.");
            return;
        }

        String url = PERMISSIONS_LIST + "?sheetName=" + selectedSheetName; // Construct the URL

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Request Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    handlePermissionData(responseBody);
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Data Error", "Failed to get permissions: " + response.message()));
                }
            }
        });
    }

    private void handlePermissionData(String responseBody) {
        Gson gson = new Gson();
        List<PermissionInfoDTO> permissions = gson.fromJson(responseBody, new TypeToken<List<PermissionInfoDTO>>(){}.getType());

        Platform.runLater(() -> {
            if (dashboardController != null) {
                dashboardController.updatePermissions(permissions);
            }
        });
    }

    @FXML
    void requestPermissionButtonListener(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("permission/permissionDialog.fxml"));
            Parent root = fxmlLoader.load();

            PermissionController permissionController = fxmlLoader.getController();

            permissionController.setCurrentSheetName(dashboardController.currentSheetNameProperty().get());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Permission Request");

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewSheetListener(ActionEvent event) {
        String selectedSheetName = dashboardController.currentSheetNameProperty().get();
        boolean isReader = dashboardController.isReaderProperty().get();
        boolean isNone = dashboardController.isNoneProperty().get();

        if (selectedSheetName == null || selectedSheetName.isEmpty()) {
            AlertUtil.showErrorAlert("Sheet Selection Error", "No sheet selected.");
            return;
        }

        if (isNone) {
            AlertUtil.showErrorAlert("Permission Denied", "You do not have permission to view this sheet.");
            return;
        }

        String url = VIEW_SHEET_URL + "?sheetName=" + selectedSheetName;

        HttpClientUtil.runAsync(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Request Error", "Failed to send request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    SpreadsheetDTO spreadsheetDTO = ADAPTED_GSON.fromJson(responseBody, SpreadsheetDTO.class);

                    Platform.runLater(() -> handleSpreadsheetData(spreadsheetDTO, isReader));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Data Error", "Failed to get spreadsheet data: " + response.message()));
                }
            }
        });
    }

    private void handleSpreadsheetData(SpreadsheetDTO spreadsheetDTO , boolean isReader) {
        Platform.runLater(() -> {
            mainController.loadApplicationPage(spreadsheetDTO , isReader);
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}


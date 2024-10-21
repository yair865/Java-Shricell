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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
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

    private StringProperty currentSheetName;

    private IntegerProperty currentRequestId = new SimpleIntegerProperty();

    @FXML
    void permissionApproveListener(ActionEvent event) {
        sendPermissionRequest(RequestStatus.APPROVED);
    }

    private void sendPermissionRequest(RequestStatus status) {
        int requestId = currentRequestId.get();
        String sheetName = currentSheetName.get();

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
        String selectedSheetName = currentSheetName.get();

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
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

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

            // Get the PermissionController from the loader
            PermissionController permissionController = fxmlLoader.getController();

            // Bind the current sheet name property
            permissionController.bindSheetName(currentSheetName);

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
        String selectedSheetName = currentSheetName.get();

        if (selectedSheetName == null || selectedSheetName.isEmpty()) {
            AlertUtil.showErrorAlert("Sheet Selection Error", "No sheet selected.");
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

                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(SpreadsheetDTO.class, new SpreadsheetDTODeserializer())
                            .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueTypeAdapter())
                            .create();

                    SpreadsheetDTO spreadsheetDTO = gson.fromJson(responseBody, SpreadsheetDTO.class);

                    Platform.runLater(() -> handleSpreadsheetData(spreadsheetDTO));
                } else {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Data Error", "Failed to get spreadsheet data: " + response.message()));
                }
            }
        });
    }

    private void handleSpreadsheetData(SpreadsheetDTO spreadsheetDTO) {
        Platform.runLater(() -> {
            mainController.loadApplicationPage(spreadsheetDTO);
        });
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void setCurrentSheetNameProperty(StringProperty currentSheetName) {
        this.currentSheetName = currentSheetName;
    }

    public void setCurrentRequestIdProperty(IntegerProperty currentRequestId) {
        this.currentRequestId = currentRequestId;
    }
}


package component.dashboard.right.permission;

import consts.Constants;
import engine.permissionmanager.PermissionType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;
import util.alert.AlertUtil;

import java.io.IOException;

public class PermissionController {

    @FXML
    private RadioButton ReaderRB;

    @FXML
    private RadioButton WriterRB;

    @FXML
    private Button cancelButton;

    @FXML
    private HBox permissionTypeVbox;

    @FXML
    private Button requestButton;

    private String currentSheetName;

    @FXML
    void initialize() {
        ReaderRB.setOnAction(this::ReaderRBListener);
        WriterRB.setOnAction(this::WriterRBListener);
    }

    @FXML
    void ReaderRBListener(ActionEvent event) {
        if (ReaderRB.isSelected()) {
            WriterRB.setSelected(false);
        }
    }

    @FXML
    void WriterRBListener(ActionEvent event) {
        if (WriterRB.isSelected()) {
            ReaderRB.setSelected(false);
        }
    }

    @FXML
    void cancelButtonListener(ActionEvent event) {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void requestButtonListener(ActionEvent event) {

        String sheetName = currentSheetName;

        if (sheetName == null || sheetName.isEmpty()) {
            AlertUtil.showErrorAlert("Permission Request Error", "No sheet selected.");
            return;
        }

        PermissionType permissionType;
        if (ReaderRB.isSelected()) {
            permissionType = PermissionType.READER;
        } else if (WriterRB.isSelected()) {
            permissionType = PermissionType.WRITER;
        } else {
            AlertUtil.showErrorAlert("Permission Request Error", "No permission type selected.");
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("sheetName", sheetName)
                .add("permissionType", permissionType.name()) // Use the enum name
                .build();


        HttpClientUtil.runAsyncPost(Constants.REQUEST_PERMISSION, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> AlertUtil.showErrorAlert("Connection Error", "Failed to send permission request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.code() != 200) {
                    Platform.runLater(() -> AlertUtil.showErrorAlert("Request Failed", responseBody));
                } else {
                    Stage stage = (Stage) requestButton.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    public void setCurrentSheetName(String currentSheetName) {
        this.currentSheetName = currentSheetName;
    }
}

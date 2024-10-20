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

    private StringProperty currentSheetName = new SimpleStringProperty();

    @FXML
    void initialize() {
        // Ensure that if one RadioButton is selected, the other gets deselected
        ReaderRB.setOnAction(this::ReaderRBListener);
        WriterRB.setOnAction(this::WriterRBListener);
    }

    @FXML
    void ReaderRBListener(ActionEvent event) {
        if (ReaderRB.isSelected()) {
            WriterRB.setSelected(false);  // Deselect WriterRB when ReaderRB is selected
        }
    }

    @FXML
    void WriterRBListener(ActionEvent event) {
        if (WriterRB.isSelected()) {
            ReaderRB.setSelected(false);  // Deselect ReaderRB when WriterRB is selected
        }
    }

    @FXML
    void cancelButtonListener(ActionEvent event) {
        // Get the current stage (window) and close it
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void bindSheetName(StringProperty dashboardSheetName) {
        currentSheetName.bind(dashboardSheetName);
    }

    @FXML
    void requestButtonListener(ActionEvent event) {
        // Get the sheet name from the bound property
        String sheetName = currentSheetName.get();

        if (sheetName == null || sheetName.isEmpty()) {
            showErrorAlert("Permission Request Error", "No sheet selected.");
            return;
        }

        PermissionType permissionType;
        if (ReaderRB.isSelected()) {
            permissionType = PermissionType.READER;
        } else if (WriterRB.isSelected()) {
            permissionType = PermissionType.WRITER;
        } else {
            showErrorAlert("Permission Request Error", "No permission type selected.");
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("sheetName", sheetName)
                .add("permissionType", permissionType.name()) // Use the enum name
                .build();


        HttpClientUtil.runAsyncPost(Constants.REQUEST_PERMISSION, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrorAlert("Connection Error", "Failed to send permission request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.code() != 200) {
                    Platform.runLater(() -> showErrorAlert("Request Failed", responseBody));
                } else {
                    Platform.runLater(() -> showSuccessAlert("Request Success", "Permission request successful!"));
                }
            }
        });
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package component.dashboard.header;

import component.main.MainController;
import constants.Constants;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import util.HttpClientUtil;

import java.io.File;
import java.io.IOException;

public class DashboardHeaderController {

    private MainController mainController;

    @FXML
    private GridPane header;

    @FXML
    private Button loadFileButton;

    @FXML
    private Label userNameLabel;

    @FXML
    void loadFileListener(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(header.getScene().getWindow());

        if (selectedFile == null) {
            showErrorAlert("File Selection Error", "No file selected. Please select an XML file.");
            return;
        }

        String userName = mainController.currentUserProperty().get();

        RequestBody fileBody = RequestBody.create(selectedFile, MediaType.parse("application/xml"));


        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", selectedFile.getName(), fileBody)
                .addFormDataPart("userName", userName)
                .build();

        HttpClientUtil.runAsyncPost(Constants.UPLOAD_FILE_PAGE, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrorAlert("Connection Error", "Failed to load file: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> System.out.println(responseBody));
                } else {
                    Platform.runLater(() -> showSuccessAlert("Load Success", "File loaded successfully."));
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

    public void setMainController(MainController mainController) {
        userNameLabel.textProperty().bind(Bindings.concat(mainController.currentUserProperty()));
        this.mainController = mainController;
    }
}

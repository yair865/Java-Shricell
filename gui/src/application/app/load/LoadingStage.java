package application.app.load;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingStage {
    private Stage loadingStage;
    private ProgressBar progressBar;
    private Label loadingLabel;

    public LoadingStage() {
        loadingStage = new Stage();
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setTitle("Loading...");

        // Create a label to show the loading message
        loadingLabel = new Label("Please wait while the file is loaded.");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        progressBar = new ProgressBar(0);
        BorderPane pane = new BorderPane();
        pane.setTop(loadingLabel);
        pane.setCenter(progressBar);

        // Set the size of the loading stage
        Scene scene = new Scene(pane, 400, 150); // Increased size
        scene.getStylesheets().add(getClass().getResource("loadingStyle.css").toExternalForm()); // Add CSS style
        loadingStage.setScene(scene);
    }

    public void showLoadingStage() {
        loadingStage.show();
    }

    public void closeLoadingStage() {
        loadingStage.close();
    }

    public void updateProgress(double progress) {
        if (loadingStage != null && loadingStage.isShowing()) {
            Platform.runLater(() -> {
                progressBar.setProgress(progress);
            });
        }
    }
}

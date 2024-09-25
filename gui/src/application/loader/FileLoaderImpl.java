package application.loader;

import application.app.ShticellController;
import dto.dtoPackage.SpreadsheetDTO;
import engine.engineimpl.Engine;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;

public class FileLoaderImpl implements FileLoader {

    private final Engine engine;
    private final ShticellController shticellController;

    public FileLoaderImpl(Engine engine, ShticellController controller) {
        this.engine = engine;
        this.shticellController = controller;
    }

    public void loadFile(String filePath) {
        ProgressBar progressBar = createProgressBar();
        Alert progressAlert = createProgressAlert(progressBar);

        Task<Void> loadTask = createLoadTask(filePath, progressBar, progressAlert);

        bindTaskToAlert(progressAlert, loadTask);
        new Thread(loadTask).start();
        progressAlert.show();
    }

    private ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);
        return progressBar;
    }

    private Alert createProgressAlert(ProgressBar progressBar) {
        Alert progressAlert = new Alert(Alert.AlertType.INFORMATION);
        progressAlert.setTitle("Loading File");
        progressAlert.setHeaderText("Please wait while the file is loading.");

        progressAlert.getButtonTypes().clear();
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        progressAlert.getButtonTypes().add(cancelButtonType);
        Button cancelButton = (Button) progressAlert.getDialogPane().lookupButton(cancelButtonType);

        BorderPane progressPane = new BorderPane();
        progressPane.setCenter(progressBar);
        progressPane.setBottom(cancelButton);
        BorderPane.setAlignment(cancelButton, Pos.CENTER);
        progressAlert.getDialogPane().setContent(progressPane);

        cancelButton.setOnAction(event -> {
        });

        return progressAlert;
    }

    private Task<Void> createLoadTask(String filePath, ProgressBar progressBar, Alert progressAlert) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 100);
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(10); // Simulating work
                    updateProgress(i, 100);
                }
                engine.loadSpreadsheet(filePath); // Your actual file loading logic here
                return null;
            }
        };
    }

    private void bindTaskToAlert(Alert progressAlert, Task<Void> loadTask) {
        ProgressBar progressBar = (ProgressBar) progressAlert.getDialogPane().getContent();
        progressBar.progressProperty().bind(loadTask.progressProperty());

        loadTask.setOnSucceeded(event -> {
            handleLoadSuccess(progressAlert);
        });

        loadTask.setOnFailed(event -> {
            handleLoadFailure(progressAlert, loadTask);
        });
    }

    private void handleLoadSuccess(Alert progressAlert) {
        shticellController.getDataManager().getCellDataMap().clear();
        SpreadsheetDTO spreadSheet = engine.getSpreadsheetState();
     //   shticellController.convertDTOToCellData(spreadSheet);
       // shticellController.updateBodyController(spreadSheet);
        shticellController.getLeftComponentController().updateRangeList(spreadSheet.ranges().keySet());
        progressAlert.close();
        System.out.println("Successfully loaded the spreadsheet.");
    }

    private void handleLoadFailure(Alert progressAlert, Task<Void> loadTask) {
        progressAlert.close();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Loading File");
        alert.setHeaderText("An error occurred while loading the file.");
        alert.setContentText("Error: " + loadTask.getException().getMessage() + "\nPlease try to load the file again.");
        alert.showAndWait();
    }
}

package application.left.sort;

import application.app.ShticellController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SortController {

    @FXML
    private BorderPane SortWindow;

    @FXML
    private Button addColumnButton;

    @FXML
    private Button removeColumnButton;

    @FXML
    private TextField cellsToSortTextField;

    @FXML
    private VBox sortBody;

    @FXML
    private VBox sortWindowHeader;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    private SimpleIntegerProperty columnCountProperty;

    private List<ColumnPickerController> columnPickerControllers;

    private ShticellController shticellController;

    public SortController() {
        columnCountProperty = new SimpleIntegerProperty(0);
        this.columnPickerControllers = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        columnCountProperty.set(sortBody.getChildren().size());
        removeColumnButton.disableProperty().bind(Bindings.lessThanOrEqual(columnCountProperty, 1));
        addColumnButton.disableProperty().bind(Bindings.greaterThanOrEqual(columnCountProperty, 5));

        sortBody.getChildren().addListener((ListChangeListener<Node>) change -> {
            columnCountProperty.set(sortBody.getChildren().size());
        });
    }


    private void addColumnPicker() {
        int numberOfColumns = 0;
        if (columnCountProperty.get() < 5) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ColumnPicker.fxml"));
                Node columnPicker = loader.load();
                ColumnPickerController controller = loader.getController();
                if (shticellController != null) {
                    numberOfColumns = shticellController.getNumberOfColumns();
                }
                controller.setColumnChoiceBox(numberOfColumns);
                sortBody.getChildren().add(columnPicker);
                columnPickerControllers.add(controller);
            } catch (IOException e) {
                showErrorAlert("Error adding column", "Failed to add a new column picker.");
            }
        }
    }

    @FXML
    void addColumnListener(ActionEvent event) {
        addColumnPicker();
    }

    @FXML
    void removeColumnListener(ActionEvent event) {
        if (!sortBody.getChildren().isEmpty()) {
            sortBody.getChildren().remove(sortBody.getChildren().size() - 1);
        }
        columnCountProperty.set(sortBody.getChildren().size());
    }

    @FXML
    void applyListener(ActionEvent event) {
        String cellsRange = cellsToSortTextField.getText().trim();
        if (cellsRange.isEmpty()) {
            showErrorAlert("Validation Error", "Cells range cannot be empty.");
            return;
        }

        List<Character> selectedColumns = columnPickerControllers.stream()
                .map(ColumnPickerController::getSelectedColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (selectedColumns.isEmpty()) {
            showErrorAlert("Validation Error", "At least one choice box must be selected.");
            return;
        }

        if (shticellController != null) {
            shticellController.sortSheet(cellsRange, selectedColumns);
        }

        closeWindow();
    }

    @FXML
    void cancelListener(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
      addColumnPicker();
    }
}

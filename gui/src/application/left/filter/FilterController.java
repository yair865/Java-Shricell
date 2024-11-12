package application.left.filter;

import application.app.ShticellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

public class FilterController {

    @FXML
    private BorderPane filterWindow;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField cellsToFilterTextField;

    @FXML
    private ChoiceBox<Character> columnChoiceBox;

    @FXML
    private HBox columnPickerComponent;

    @FXML
    private Button pickValuesButton;

    @FXML
    private VBox filterBody;

    @FXML
    private VBox filterWindowHeader;

    private ShticellController shticellController;
    private List<String> selectedValues;

    @FXML
    void initialize() {
        pickValuesButton.setDisable(true);

        columnChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            pickValuesButton.setDisable(newValue == null);
        });
    }

    @FXML
    void applyListener(ActionEvent event) {
        Character selectedColumn = getSelectedColumn();
        if (selectedColumn == null) {
            showAlert("No column selected", "Please select a column before applying the filter.");
            return;
        }

        String filterArea = cellsToFilterTextField.getText();
        if (selectedValues == null || selectedValues.isEmpty()) {
            showAlert("No values selected", "Please select at least one value before applying the filter.");
            return;
        }

        shticellController.applyFilter(selectedColumn, filterArea, selectedValues);
        closeWindow();
    }

    @FXML
    void cancelListener(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void pickValuesListener(ActionEvent event) {
        List<String> effectiveValues = shticellController.getEffectiveValuesForColumn(getSelectedColumn());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ValuePickerWindow.fxml"));
            Parent root = loader.load();

            ValuePickerController valuePickerController = loader.getController();
            valuePickerController.setCheckBoxValues(effectiveValues);
            valuePickerController.setFilterController(this);

            Stage valuePickerStage = new Stage();
            valuePickerStage.setTitle("Select Values");
            valuePickerStage.initModality(Modality.APPLICATION_MODAL);
            valuePickerStage.setScene(new Scene(root, 400, 300));

            valuePickerStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while opening the Value Picker Window: " + e.getMessage());
        }
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setSelectedValues(List<String> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public void setColumnChoiceBox(int numberOfColumns) {
        for (char c = 'A'; c < 'A' + numberOfColumns; c++) {
            columnChoiceBox.getItems().add(c);
        }
    }

    public Character getSelectedColumn() {
        return columnChoiceBox.getSelectionModel().getSelectedItem();
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
        setColumnChoiceBox(shticellController.getNumberOfColumns());
    }
}

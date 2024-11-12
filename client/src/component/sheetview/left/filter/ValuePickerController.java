package component.sheetview.left.filter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ValuePickerController {

    @FXML
    private VBox checkboxContainer;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    private List<String> selectedValues = new ArrayList<>();
    private FilterController filterController;

    @FXML
    void okListener(ActionEvent event) {
        selectedValues.clear();

        for (var node : checkboxContainer.getChildren()) {
            if (node instanceof CheckBox checkbox && checkbox.isSelected()) {
                selectedValues.add(checkbox.getText());
            }
        }

        if (selectedValues.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No selection made");
            alert.setContentText("Please select at least one value before proceeding.");
            alert.showAndWait();
            return;
        }

        if (filterController != null) {
            filterController.setSelectedValues(selectedValues);
        }

        closeWindow();
    }

    @FXML
    void cancelListener(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void setCheckBoxValues(List<String> values) {
        for (String value : values) {
            CheckBox checkBox = new CheckBox(value);
            checkboxContainer.getChildren().add(checkBox);
        }
    }

    public void setFilterController(FilterController filterController) {
        this.filterController = filterController;
    }
}

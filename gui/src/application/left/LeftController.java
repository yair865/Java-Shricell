package application.left;

import application.app.ShticellController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.function.Consumer;

public class LeftController {

    @FXML
    private Button setColorBTN;

    @FXML
    private Button setColumnWidthBTN;

    @FXML
    private Button setRowHeightBTN;

    @FXML
    private ComboBox<String> setAlignmentComboBox;

    private ShticellController shticellController;

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }

    @FXML
    private void initialize() {
        ObservableList<String> options =
                FXCollections.observableArrayList("Left", "Center", "Right");
        setAlignmentComboBox.setItems(options);

    }

    private void showInputDialog(String title, String header, String content, Consumer<Integer> onSuccess) {
        // Create a TextInputDialog for input
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        // Show the dialog and capture the result
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                int newValue = Integer.parseInt(value);
                onSuccess.accept(newValue);
            } catch (NumberFormatException e) {
                // Handle invalid number input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter a valid number.");
                alert.showAndWait();
            }
        });
    }

    @FXML
    void setColumnWidthListener(ActionEvent event)    {
        showInputDialog(
                "Set Column Width",
                "Enter the new height for the row:",
                "Height:",
                newWidth -> {
                    // Call the logic to set the row height
                    this.shticellController.getBodyController().setColumnWidth(newWidth);
                }
        );
    }

    @FXML
    void setRowHeightListener(ActionEvent event)
    {
        showInputDialog(
                "Set Column Width",
                "Enter the new width for the column:",
                "Width:",
                newHeight -> {
                    // Call the logic to set the column width
                    this.shticellController.getBodyController().setRowHeight(newHeight);
                }
        );
    }

    @FXML
    void updateCellColorListener(ActionEvent event) {
        String cellId = this.shticellController.getHeaderController().getCellId();

    }

    @FXML
    void setAlignmentListener(ActionEvent event) {
        int selectedIndex = setAlignmentComboBox.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                this.shticellController.getBodyController().alignCells(javafx.geometry.Pos.CENTER_LEFT);
                break;
            case 1:
                this.shticellController.getBodyController().alignCells(Pos.CENTER);
                break;
            case 2:
                this.shticellController.getBodyController().alignCells(Pos.CENTER_RIGHT);
                break;
        }
    }
}



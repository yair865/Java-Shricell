package application.left;

import application.app.ShticellController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.function.Consumer;

public class LeftController {

    @FXML
    private Button setBackgroundColorBTN;

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
    void setColumnWidthListener(ActionEvent event) {
        showInputDialog(
                "Set Column Width",
                "Enter the new width for the column:",
                "Width:",
                newWidth -> {
                    // Call the logic to set the column width
                    this.shticellController.getBodyController().setColumnWidth(newWidth);
                }
        );
    }

    @FXML
    void setRowHeightListener(ActionEvent event) {
        showInputDialog(
                "Set Row Height",
                "Enter the new height for the row:",
                "Height:",
                newHeight -> {
                    // Call the logic to set the row height
                    this.shticellController.getBodyController().setRowHeight(newHeight);
                }
        );
    }

    @FXML
    void updateTextColorListener(ActionEvent event) {
        ColorPicker colorPicker = new ColorPicker(Color.BLACK); // Default color

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select Text Color");
        alert.setHeaderText("Choose a color for the text:");
        alert.getDialogPane().setContent(colorPicker);

        // Show the alert and capture the result
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Color selectedColor = colorPicker.getValue();
            String cellId = this.shticellController.getHeaderController().getCellId();
            this.shticellController.getEngine().setSingleCellTextColor(cellId, toHexString(selectedColor));
        }
    }

    @FXML
    void updateBackGroundColorListener(ActionEvent event) {
        ColorPicker colorPicker = new ColorPicker(Color.WHITE); // Default color

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select Background Color");
        alert.setHeaderText("Choose a color for the background:");
        alert.getDialogPane().setContent(colorPicker);

        // Show the alert and capture the result
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Color selectedColor = colorPicker.getValue();
            String cellId = this.shticellController.getHeaderController().getCellId();
            this.shticellController.getEngine().setSingleCellBackGroundColor(cellId, toHexString(selectedColor));
        }
    }

    private String toHexString(Color color) {
        return String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
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



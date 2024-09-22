package application.left;

import application.app.ShticellController;
import engine.api.Coordinate;
import engine.sheetimpl.cellimpl.coordinate.CoordinateFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Button resetStyleBTN;

    @FXML
    private ComboBox<String> setAlignmentComboBox;

    @FXML
    private Button addRangeButton;

    @FXML
    private Button deleteRangeButton;

    @FXML
    private ListView<String> rangesList;


    private ShticellController shticellController;

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }

    @FXML
    private void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList("Left", "Center", "Right");
        setAlignmentComboBox.setItems(options);

        // Add selection listener to the rangesList
        rangesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                shticellController.getBodyController().highlightSelectedRange(newValue);
            }
        });

        // Add focus listener to clear selection when it loses focus
        rangesList.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If it loses focus
                clearSelection(); // Clear selection
            }
        });
    }

    public void clearSelection() {
        rangesList.getSelectionModel().clearSelection();
    }

    private void showInputDialog(String title, String header, String content, Consumer<Integer> onSuccess) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                int newValue = Integer.parseInt(value);
                onSuccess.accept(newValue);
            } catch (NumberFormatException e) {
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
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select Text Color");
        alert.setHeaderText("Choose a color for the text:");
        alert.getDialogPane().setContent(colorPicker);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Color selectedColor = colorPicker.getValue();
            String cellId = this.shticellController.getHeaderController().getCellId();
            this.shticellController.getEngine().setSingleCellTextColor(cellId, toHexString(selectedColor));

            this.shticellController.getBodyController().updateCellTextColor(cellId, toHexString(selectedColor));
        }
    }

    @FXML
    void updateBackGroundColorListener(ActionEvent event) {
        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Select Background Color");
        alert.setHeaderText("Choose a color for the background:");
        alert.getDialogPane().setContent(colorPicker);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Color selectedColor = colorPicker.getValue();
            String cellId = this.shticellController.getHeaderController().getCellId();
            this.shticellController.getEngine().setSingleCellBackGroundColor(cellId, toHexString(selectedColor));
            this.shticellController.getBodyController().updateCellBackgroundColor(cellId, toHexString(selectedColor));
        }
    }

    private String toHexString(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
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

    @FXML
    void resetStyleListener(ActionEvent event) {
        String cellId = this.shticellController.getHeaderController().getCellId();

        //Reset text color
        this.shticellController.getEngine().setSingleCellTextColor(cellId, null);
        this.shticellController.getBodyController().updateCellTextColor(cellId, null);

        //Reset background color
        this.shticellController.getEngine().setSingleCellBackGroundColor(cellId, null);
        this.shticellController.getBodyController().updateCellBackgroundColor(cellId, null);
    }

    @FXML
    void addRangeButtonListener(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RangeWindow.fxml"));
            Parent root = loader.load();

            RangeController rangeController = loader.getController();
            rangeController.setLeftController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Range");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addRangeButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRangeToList(String rangeName) {
        rangesList.getItems().add(rangeName);
    }

    @FXML
    void deleteRangeListener(ActionEvent event) {
        String selectedRange = rangesList.getSelectionModel().getSelectedItem();
        if (selectedRange != null) {
            shticellController.getEngine().removeRangeFromSheet(selectedRange);
            rangesList.getItems().remove(selectedRange);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Range Selected");
            alert.setContentText("Please select a range to delete.");
            alert.showAndWait();
        }
    }

    public void pushRangeToSheet(String rangeName, String coordinates) {
        try {
            shticellController.getEngine().addRangeToSheet(rangeName, coordinates);
        } catch (Exception e) {
            System.err.println("Failed to push range to sheet: " + e.getMessage());
        }
    }

    public void rangesListSelectionChanged() {
        String selectedRange = rangesList.getSelectionModel().getSelectedItem();
        if (selectedRange != null) {
            shticellController.getBodyController().highlightSelectedRange(selectedRange);
        }
    }
}



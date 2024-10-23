package component.sheetview.left;

import component.sheetview.app.ShticellController;
import component.sheetview.error.ErrorDisplay;
import component.sheetview.left.filter.FilterController;
import component.sheetview.left.range.RangeController;
import component.sheetview.left.sort.SortController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class LeftController {

    @FXML
    private Button setBackgroundColorBTN;

    @FXML
    private Button setTextColorBTN;

    @FXML
    private Button setColumnWidthBTN;

    @FXML
    private Button setRowHeightBTN;

    @FXML
    private Button resetStyleBTN;

    @FXML
    private Button filterButton;

    @FXML
    private Button sortButton;

    @FXML
    private Button leftAlignment;

    @FXML
    private Button rightAlignment;

    @FXML
    private Button centerAlignment;

    @FXML
    private Button addRangeButton;

    @FXML
    private Button deleteRangeButton;

    @FXML
    private ListView<String> rangesList;

    private ShticellController shticellController;

    @FXML
    private VBox leftComponent;

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
        setBackgroundColorBTN.disableProperty().bind(shticellController.isReaderProperty());
        setTextColorBTN.disableProperty().bind(shticellController.isReaderProperty());
        setColumnWidthBTN.disableProperty().bind(shticellController.isReaderProperty());
        setRowHeightBTN.disableProperty().bind(shticellController.isReaderProperty());
        resetStyleBTN.disableProperty().bind(shticellController.isReaderProperty());
        addRangeButton.disableProperty().bind(shticellController.isReaderProperty());
        deleteRangeButton.disableProperty().bind(shticellController.isReaderProperty());
        rangesList.disableProperty().bind(shticellController.isReaderProperty());
    }

    @FXML
    private void initialize() {
        rangesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                shticellController.getBodyController().highlightSelectedRange(newValue);
            }
        });

        rangesList.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !deleteRangeButton.isPressed()) {
                clearSelection();
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
                new ErrorDisplay("Invalid Input", "Please enter a valid number.").showError(null);
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
            this.shticellController.setSingleCellTextColor(cellId, toHexString(selectedColor));
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
            this.shticellController.setSingleCellBackGroundColor(cellId, toHexString(selectedColor));
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
    void leftAlignmentListener(ActionEvent event) {
        this.shticellController.getBodyController().alignCells(Pos.CENTER_LEFT);
    }

    @FXML
    void centerAlignmentListener(ActionEvent event) {
        this.shticellController.getBodyController().alignCells(Pos.CENTER);
    }

    @FXML
    void rightAlignmentListener(ActionEvent event) {
        this.shticellController.getBodyController().alignCells(Pos.CENTER_RIGHT);
    }

    @FXML
    void resetStyleListener(ActionEvent event) {
        String cellId = this.shticellController.getHeaderController().getCellId();

        if (cellId == null || cellId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Cell Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a cell before resetting styles.");
            alert.showAndWait();
            return;
        }

        this.shticellController.setSingleCellTextColor(cellId, null);
        this.shticellController.getBodyController().updateCellTextColor(cellId, null);
        this.shticellController.setSingleCellBackGroundColor(cellId, null);
        this.shticellController.getBodyController().updateCellBackgroundColor(cellId, null);
    }

    @FXML
    void addRangeButtonListener(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("range/rangeWindow.fxml"));
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
            try {
                shticellController.removeRangeFromSheet(selectedRange);
                rangesList.getItems().remove(selectedRange);
                shticellController.getBodyController().clearHighlightedCells();
                clearSelection();
            } catch (IllegalStateException e) {
                new ErrorDisplay("Range In Use", "").showError(e.getMessage()); // remove that
            }
        } else {
            new ErrorDisplay("No Selection", "Please select a range to delete.").showError("");
        }
    }

    public void pushRangeToSheet(String rangeName, String coordinates) {
        try {
            shticellController.addRangeToSheet(rangeName, coordinates);
            this.addRangeToList(rangeName);
        } catch (Exception e) {
            showRangeExistsAlert(rangeName);
        }
    }

    public void rangesListSelectionChanged() {
        String selectedRange = rangesList.getSelectionModel().getSelectedItem();
        if (selectedRange != null) {
            shticellController.getBodyController().highlightSelectedRange(selectedRange);
        }
    }

    private void showRangeExistsAlert(String rangeName) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Range Exists");
        alert.setHeaderText("Duplicate Range Name");
        alert.setContentText("A range with the name '" + rangeName + "' already exists. Please choose a different name.");
        alert.showAndWait();
    }

    @FXML
    void sortListener(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sort/SortWindow.fxml"));
            Parent root = loader.load();
            SortController sortController = loader.getController();
            sortController.setShticellController(this.shticellController);
            Stage sortStage = new Stage();
            sortStage.setTitle("Sort Options");
            sortStage.initModality(Modality.APPLICATION_MODAL);
            sortStage.setScene(new Scene(root));
            sortStage.initOwner(sortButton.getScene().getWindow());
            sortStage.showAndWait();
        } catch (IOException e) {
            new ErrorDisplay("Unable to open Sort Window", "An error occurred while opening the Sort Window:").showError(e.getMessage());
        }
    }

    @FXML
    void filterListener(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("filter/FilterWindow.fxml"));
            Parent root = loader.load();
            FilterController filterController = loader.getController();
            filterController.setShticellController(this.shticellController);
            Stage filterStage = new Stage();
            filterStage.setTitle("Filter Options");
            filterStage.initModality(Modality.APPLICATION_MODAL);
            filterStage.setScene(new Scene(root));
            filterStage.initOwner(filterButton.getScene().getWindow());
            filterStage.showAndWait();
        } catch (IOException e) {
            new ErrorDisplay("Unable to open Filter Window", "An error occurred while opening the Filter Window:").showError(e.getMessage());
        }
    }

    public void updateRangeList(Set<String> strings) {
        rangesList.getItems().clear();
        strings.forEach(rangesList.getItems()::add);
    }

    public void setSkin(String name) {
        leftComponent.getStylesheets().clear();

        switch (name) {
            case "Default":
                leftComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/leftDefault.css")).toExternalForm());
                break;
            case "Thunder Cats":
                leftComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/leftThunderCats.css")).toExternalForm());
                break;
            case "India":
                leftComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/leftIndia.css")).toExternalForm());
                break;
        }
    }
}



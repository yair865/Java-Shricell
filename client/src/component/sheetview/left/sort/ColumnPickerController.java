package component.sheetview.left.sort;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class ColumnPickerController {

    @FXML
    private ChoiceBox<Character> columnChoiceBox;

    @FXML
    private void initialize() {
    }

    public void setColumnChoiceBox(int numberOfColumns) {
        for (char c = 'A'; c < 'A' + numberOfColumns; c++) {
            columnChoiceBox.getItems().add(c);
        }
    }

    public Character getSelectedColumn() {
        return columnChoiceBox.getSelectionModel().getSelectedItem();
    }
}

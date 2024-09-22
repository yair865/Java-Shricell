package application.header;

import application.app.ShticellController;
import engine.api.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    private ShticellController shticellController;

    private Engine engine;

    @FXML
    private Button btnFileChooser;

    @FXML
    private TextField filePathTextField;

    @FXML
    private TextField newValueTextField;

    @FXML
    private Button updateNewValueBTN;

    @FXML
    private Label cellIdLabel;

    @FXML
    private Label cellOriginalValueLabel;

    @FXML
    private Label cellVersionLabel;

    @FXML
    private ChoiceBox<Integer> selectVersionChoiceBox;

    private static final String DEFAULT_NEW_VALUE_PROMPT = "Enter new value";

    @FXML
    private void initialize() {
        selectVersionChoiceBox.setOnAction(event -> {
            Integer selectedVersion = selectVersionChoiceBox.getValue();
            if (selectedVersion != null) {
                this.shticellController.showSpreadsheetVersion(selectedVersion);
                selectVersionChoiceBox.getSelectionModel().clearSelection();
            }
        });
    }

    @FXML
    public void loadFileButtonListener() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) btnFileChooser.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            filePathTextField.setText(filePath);
            shticellController.loadFile(filePath);
        }
    }

    @FXML
    void updateValueButtonListener(ActionEvent event) {
        String newValue = newValueTextField.getText();
        String cellId = cellIdLabel.getText();

        if (newValue != null && !newValue.trim().isEmpty()
                && cellId != null && !cellId.trim().isEmpty()) {
            shticellController.updateNewEffectiveValue(cellId, newValue);
        }

        newValueTextField.clear();
        resetHeaderLabels();
    }

    private void resetHeaderLabels() {
        newValueTextField.setPromptText(DEFAULT_NEW_VALUE_PROMPT);
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }

    public void updateHeader(String cellId, String originalValue, int lastModifiedVersion) {
        this.cellIdLabel.setText(cellId);
        this.cellOriginalValueLabel.setText(originalValue);
        this.cellVersionLabel.setText(String.valueOf(lastModifiedVersion));
    }

    public TextField getNewValueTextField() {
        return this.newValueTextField;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setVersionsChoiceBox() {
        selectVersionChoiceBox.getItems().add(engine.getCurrentVersion());
    }

    public char getSelectedCellColumn() {
        return cellIdLabel.getText().charAt(0);
    }

    public int getSelectedCellRow()
    {
        return Integer.parseInt(cellIdLabel.getText().substring(1,2));
    }

    public String getCellId() {
        return cellIdLabel.getText();
    }

    public void setCellOriginalValueLabel(String originalValue) {
        this.cellOriginalValueLabel.setText(originalValue);
    }
}



package application.header;

import application.app.ShticellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    private ShticellController shticellController;

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

    private static final String DEFAULT_ID = "Selected Cell Id";

    private static final String DEFAULT_ORIGINAL_VALUE = "Original Cell Value";

    private static final String DEFAULT_VERSION = "Last modified version";

    private static final String DEFAULT_NEW_VALUE_PROMPT = "Enter new value";

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
        resetHeaderLabels(); // Reset labels to default text
    }

    private void resetHeaderLabels() {
        cellIdLabel.setText(DEFAULT_ID);
        cellOriginalValueLabel.setText(DEFAULT_ORIGINAL_VALUE);
        cellVersionLabel.setText(DEFAULT_VERSION);
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
}



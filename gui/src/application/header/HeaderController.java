package application.header;

import application.app.ShticellController;
import engine.engineimpl.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.Objects;

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

    @FXML
    private ComboBox<SkinItem> skinComboBox; // Use SkinItem instead of Image

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

        skinComboBox.setOnAction(event -> {
            SkinItem selectedSkin = skinComboBox.getValue();
            if (selectedSkin != null) {
                applySkin(selectedSkin.getImage());
            }
        });

        addSkinIcons();
        configureSkinComboBox();
    }

    private void addSkinIcons() {
        skinComboBox.getItems().addAll(
                new SkinItem("Default", new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/defaultIcon.png")))),
                new SkinItem("Thunder Cats", new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/thunderCatsIcon.png")))),
                new SkinItem("India", new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/indiaIcon.png"))))
        );
    }

    private void configureSkinComboBox() {
        skinComboBox.setCellFactory(new Callback<ListView<SkinItem>, ListCell<SkinItem>>() {
            @Override
            public ListCell<SkinItem> call(ListView<SkinItem> param) {
                return new ListCell<SkinItem>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(SkinItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            imageView.setImage(item.getImage());
                            imageView.setFitHeight(30);
                            imageView.setFitWidth(30);
                            setGraphic(imageView);
                            setText(item.getName()); // Set the name next to the icon
                        }
                    }
                };
            }
        });

        skinComboBox.setButtonCell(new ListCell<SkinItem>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(SkinItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    imageView.setImage(item.getImage());
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(30);
                    setGraphic(imageView);
                    setText(item.getName()); // Set the name for the button cell
                }
            }
        });
    }

    private void applySkin(Image selectedSkin) {
        String cssFile = ""; // Determine CSS file based on selectedSkin
        Stage stage = (Stage) btnFileChooser.getScene().getWindow();
        Scene scene = stage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
    }

    @FXML
    public void loadFileButtonListener() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) btnFileChooser.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            shticellController.loadFile(filePath);
            selectVersionChoiceBox.getItems().clear();
        }
    }

    @FXML
    void updateValueButtonListener(ActionEvent event) {
        String newValue = newValueTextField.getText();
        String cellId = cellIdLabel.getText();

        if (cellId != null && !cellId.trim().isEmpty()) {
            shticellController.updateNewEffectiveValue(cellId, newValue);
            newValueTextField.clear();
            resetHeaderLabels();
        }
    }

    private void resetHeaderLabels() {
        newValueTextField.setPromptText(DEFAULT_NEW_VALUE_PROMPT);
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
        updateNewValueBTN.disableProperty().bind(shticellController.isFileLoadedProperty().not());
        newValueTextField.disableProperty().bind(shticellController.isFileLoadedProperty().not());
        selectVersionChoiceBox.disableProperty().bind(shticellController.isFileLoadedProperty().not());
    }

    public void updateHeader(String cellId, String originalValue, int lastModifiedVersion) {
        this.cellIdLabel.setText(cellId);
        this.cellOriginalValueLabel.setText(originalValue);
        this.cellVersionLabel.setText(String.valueOf(lastModifiedVersion));
    }

    public TextField getNewValueTextField() {
        return this.newValueTextField;
    }

    public void setPathTextField(String path) {
        this.filePathTextField.setText(path);
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

    public int getSelectedCellRow() {
        return Integer.parseInt(cellIdLabel.getText().substring(1));
    }

    public String getCellId() {
        return cellIdLabel.getText();
    }

    public void setCellOriginalValueLabel(String originalValue) {
        this.cellOriginalValueLabel.setText(originalValue);
    }

    // Inner class to hold image and name
    public static class SkinItem {
        private final String name;
        private final Image image;

        public SkinItem(String name, Image image) {
            this.name = name;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public String toString() {
            return name; // Display name in ComboBox
        }
    }
}

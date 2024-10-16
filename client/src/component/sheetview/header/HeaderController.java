package component.sheetview.header;

import component.sheetview.app.ShticellController;
import engine.sheetmanager.SheetManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.Objects;

public class HeaderController {

    private ShticellController shticellController;
    private SheetManager engine;

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
    private ComboBox<SkinItem> skinComboBox;

    @FXML
    private GridPane headerComponent;

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
                shticellController.changeTheme(selectedSkin.getName());
            }
        });

        addSkinIcons();
        skinComboBox.getSelectionModel().selectFirst();
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
                            setText(item.getName());
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
                    setText(item.getName());
                }
            }
        });
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
    }

    public void updateHeader(String cellId, String originalValue, int lastModifiedVersion) {
        this.cellIdLabel.setText(cellId);
        this.cellOriginalValueLabel.setText(originalValue);
        this.cellVersionLabel.setText(String.valueOf(lastModifiedVersion));
    }

    public TextField getNewValueTextField() {
        return this.newValueTextField;
    }

    public void setEngine(SheetManager engine) {
        this.engine = engine;
    }

    public void setVersionsChoiceBox() {
        //selectVersionChoiceBox.getItems().add(engine.getCurrentVersion());
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

    public void setSkin(String name) {
        headerComponent.getStylesheets().clear();

        switch (name) {
            case "Default":
                headerComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/headerDefault.css")).toExternalForm());
                break;
            case "Thunder Cats":
                headerComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/headerThunderCats.css")).toExternalForm());
                break;
            case "India":
                headerComponent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("skin/headerIndia.css")).toExternalForm());
                break;
        }
    }

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
            return name;
        }
    }
}

package application.body;

import application.app.ShticellController;
import application.header.HeaderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class CellViewController extends BasicCellData {

    private ShticellController shticellController;

    @FXML
    private StackPane cellView;

    @FXML
    public Label effectiveValueLabel;

    public CellViewController() {
        super("", "Cell Original Value", "Cell ID");
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }

    @FXML
    private void initialize() {
        effectiveValueLabel.textProperty().bind(effectiveValue);
    }

    @FXML
    void cellClickListener(MouseEvent event) {
        if (event.getClickCount() == 1) {
            if (shticellController != null) {
                HeaderController headerController = shticellController.getHeaderController();
                if (headerController != null) {
                    headerController.updateHeader(
                            cellId.get(),
                            originalValue.get(),
                            lastModifiedVersion.get()
                    );
                    headerController.getNewValueTextField().requestFocus();
                }
            }
        }
    }

    public void setCellId(String cellId) {
        this.cellId.set(cellId);
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue.set(originalValue);
    }

    public void setLastModifiedVersion(int lastModifiedVersion) {
        this.lastModifiedVersion.set(lastModifiedVersion);
    }

    public void setEffectiveValue(String effectiveValue) {
        this.effectiveValue.set(effectiveValue);
    }
}

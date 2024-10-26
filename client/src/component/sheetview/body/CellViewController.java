package component.sheetview.body;

import component.sheetview.app.ShticellController;
import component.sheetview.header.HeaderController;
import engine.sheetimpl.utils.CellType;
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
        super("", "Cell Original Value", "Cell ID", null, null ,false , CellType.EMPTY);
    }

    public void setShticellController(ShticellController shticellController) {
        this.shticellController = shticellController;
    }

    @FXML
    private void initialize() {
        effectiveValueLabel.textProperty().bind(effectiveValue);
    }

    @FXML
    public void cellClickListener(MouseEvent event) {
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

                    shticellController.getBodyController().highlightDependencies(cellId.get());
                    shticellController.getBodyController().highlightDependents(cellId.get());

                    cellView.getStyleClass().add("selected-cell");
                    shticellController.getBodyController().addHighlightedCell(cellId.getValue());
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

    public void updateCellStyle() {
        if (backgroundColor.getValue() != null) {
            cellView.setStyle("-fx-background-color: " + backgroundColor.getValue() + ";");
        }
        if (textColor.getValue() != null) {
            effectiveValueLabel.setStyle("-fx-text-fill: " + textColor.getValue() + ";");
        }
    }
}

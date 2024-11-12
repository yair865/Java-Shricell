package component.sheetview.error;

import javafx.scene.control.Alert;

public class ErrorDisplay {
    private String headerText;
    private String contentText;

    public ErrorDisplay(String headerText, String contentText) {
        this.headerText = headerText;
        this.contentText = contentText;
    }

    public void showError(String exceptionMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText + "\n" + exceptionMessage);
        alert.showAndWait();
    }
}

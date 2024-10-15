package component.dashboard.body;

import component.dashboard.body.permissionListArea.PermissionListController;
import component.dashboard.body.sheetListArea.SheetsListController;
import component.main.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DashboardBodyController {

    MainController mainController;
    @FXML VBox sheetsListComponent;
    @FXML private SheetsListController sheetsListComponentController;
    @FXML VBox permissionListComponent;
    @FXML private PermissionListController permissionListComponentController;

    @FXML public void initialize() {

    }

    public SheetsListController getSheetListController() {
        return this.sheetsListComponentController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setActive() {
        sheetsListComponentController.startListRefresher();
    }
}

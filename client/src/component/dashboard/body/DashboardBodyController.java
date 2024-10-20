package component.dashboard.body;

import component.dashboard.DashboardController;
import component.dashboard.body.permissionListArea.PermissionListController;
import component.dashboard.body.sheetListArea.SheetsListController;
import component.main.MainController;
import dto.dtoPackage.PermissionInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class DashboardBodyController implements Closeable {

    private DashboardController dashboardController;

    @FXML private VBox sheetsListComponent;
    @FXML private SheetsListController sheetsListComponentController;
    @FXML VBox permissionListComponent;
    @FXML private PermissionListController permissionListComponentController;

    @FXML public void initialize() {

    }

    public SheetsListController getSheetListController() {
        return this.sheetsListComponentController;
    }

    public void setActive() {
        sheetsListComponentController.startListRefresher();
    }

    @Override
    public void close() throws IOException {
        sheetsListComponentController.close();
        permissionListComponentController.close();
    }

    public PermissionListController getPermissionListController() {
        return permissionListComponentController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
        sheetsListComponentController.setDashboardController(this.dashboardController);
        permissionListComponentController.setDashboardController(this.dashboardController);
    }


    public void updatePermissions(List<PermissionInfoDTO> permissionInfos) {
        permissionListComponentController.updatePermissions(permissionInfos);
    }
}

package component.dashboard.right;

import component.main.MainController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DashboardRightController {

    private MainController mainController;

    @FXML
    private VBox dashboardRight;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}

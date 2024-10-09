package component.dashboard.body;

import component.main.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class DashboardBodyController {

    MainController mainController;

    @FXML
    private GridPane dashBoardBody;

    @FXML
    private TableView<String> permissionTableView;

    @FXML
    private TableView<String> sheetsTableView;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}

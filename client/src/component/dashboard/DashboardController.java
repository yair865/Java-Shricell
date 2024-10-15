package component.dashboard;

import component.dashboard.body.DashboardBodyController;
import component.dashboard.header.DashboardHeaderController;
import component.dashboard.right.DashboardRightController;
import component.main.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.w3c.dom.events.Event;

import static constants.Constants.UNKNOWN;

public class DashboardController {

    @FXML private DashboardHeaderController headerController;
    @FXML private GridPane header;
    @FXML private DashboardBodyController bodyController;
    @FXML private GridPane body;
    @FXML private DashboardRightController rightController;
    @FXML private HBox right;
    @FXML private Button logOutButton;
    private MainController mainController;

    private final StringProperty currentUserName;

    public DashboardController() {
        currentUserName = new SimpleStringProperty(UNKNOWN);
    }

    @FXML public void initialize() {
    }

    public void setInActive() {
    }

    public void setShticellController(MainController mainController) {
        this.mainController = mainController;
        headerController.setMainController(this.mainController);
        bodyController.setMainController(this.mainController);
        rightController.setMainController(this.mainController);
    }

    @FXML public void logOutListener(ActionEvent event) {
        mainController.switchToLogin();
    }

    public void setActive(){
        bodyController.setActive();
    }
}

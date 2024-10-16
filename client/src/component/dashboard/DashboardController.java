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

import java.io.Closeable;
import java.io.IOException;

import static consts.Constants.UNKNOWN;

public class DashboardController implements Closeable {

    @FXML
    private DashboardHeaderController headerController;
    @FXML private GridPane header;
    @FXML private DashboardBodyController bodyController;
    @FXML private GridPane body;
    @FXML private DashboardRightController rightController;
    @FXML private HBox right;
    @FXML private Button logOutButton;
    private MainController mainController;

    private StringProperty currentSheetName = new SimpleStringProperty();

    private final StringProperty currentUserName;

    public DashboardController() {
        currentUserName = new SimpleStringProperty(UNKNOWN);
    }

    @FXML public void initialize() {
        initializeSheetSelectionBinding();

    }

    private void initializeSheetSelectionBinding() {
        currentSheetName.bind(bodyController.getSheetListController().currentSheetNameProperty());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        headerController.setMainController(this.mainController);
        bodyController.setMainController(this.mainController);
        rightController.setMainController(this.mainController);
        rightController.setCurrentSheetNameProperty(currentSheetName);
    }

    @FXML public void logOutListener(ActionEvent event) {
        mainController.switchToLogin();
    }

    public void setActive(){
        bodyController.setActive();
    }

    @Override
    public void close() throws IOException {
        bodyController.close();
    }

    public String getCurrentSheetName() {
        return currentSheetName.get();
    }

    public StringProperty currentSheetNameProperty() {
        return currentSheetName;
    }

    public void setCurrentSheetName(String currentSheetName) {
        this.currentSheetName.set(currentSheetName);
    }

    public String CurrentUserNameProperty() {
        return currentUserName.get();
    }
}

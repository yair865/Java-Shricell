package component.dashboard;

import component.dashboard.body.DashboardBodyController;
import component.dashboard.body.permissionListArea.PermissionListController;
import component.dashboard.body.sheetListArea.SheetsListController;
import component.dashboard.header.DashboardHeaderController;
import component.dashboard.right.DashboardRightController;
import component.main.MainController;
import dto.dtoPackage.PermissionInfoDTO;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

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
    private IntegerProperty currentRequestId = new SimpleIntegerProperty();
    private BooleanProperty isReader = new SimpleBooleanProperty();
    private BooleanProperty isNone = new SimpleBooleanProperty();

    private final StringProperty currentUserName;

    public DashboardController() {
        currentUserName = new SimpleStringProperty(UNKNOWN);
    }

    @FXML public void initialize() {
        if(rightController != null && bodyController != null && headerController != null) {
            rightController.setDashboardController(this);
            bodyController.setDashboardController(this);
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        headerController.setMainController(this.mainController);
        //bodyController.setMainController(this.mainController);
        rightController.setMainController(this.mainController);
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

    public StringProperty currentSheetNameProperty() {return currentSheetName;}

    public BooleanProperty isReaderProperty() {return isReader;}

    public BooleanProperty isNoneProperty() {return isNone;}

    public IntegerProperty currentRequestIdProperty() {return currentRequestId;}


    public void refreshPermissions(String sheetName) {
        rightController.refreshPermissionTableButtonListener(null);
    }

    public void updatePermissions(List<PermissionInfoDTO> permissionInfos) {
        bodyController.updatePermissions(permissionInfos);
    }
}

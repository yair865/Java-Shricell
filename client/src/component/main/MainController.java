package component.main;

import component.dashboard.DashboardController;
import component.dashboard.body.sheetListArea.sheetdata.SingleSheetData;
import component.login.LoginController;
import component.sheetview.app.ShticellController;
import dto.dtoPackage.SpreadsheetDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static consts.Constants.*;

public class MainController implements Closeable {

    private Stage stage;

    @FXML private ScrollPane dashboardWindow;
    @FXML private ScrollPane applicationWindow;
    @FXML private ShticellController shticellController;
    @FXML private GridPane loginWindow;
    @FXML private LoginController loginController;
    @FXML private DashboardController dashboardController;
    @FXML private StackPane mainPanel;

    private StringProperty currentUser;

    public MainController() {
        currentUser = new SimpleStringProperty();
    }

    @FXML public void initialize() {
        loadLoginPage();
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginWindow = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setShticellController(this);
            setMainPanelTo(loginWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToLogin () {
        Platform.runLater(() -> {
            //dashboardController.setInActive();
            setMainPanelTo(loginWindow);
        });
    }

    public void loadDashboardPage() {
        URL dashboardPageUrl = getClass().getResource(DASHBOARD_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(dashboardPageUrl);
            dashboardWindow = fxmlLoader.load();
            dashboardController = fxmlLoader.getController();
            dashboardController.setMainController(this);
            setMainPanelTo(dashboardWindow);
            dashboardController.setActive();
            stage.setWidth(dashboardWindow.getPrefWidth());
            stage.setHeight(dashboardWindow.getPrefHeight() + 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadApplicationPage(SpreadsheetDTO spreadsheetDTO) {
        URL applicationPageUrl = getClass().getResource(APPLICATION_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(applicationPageUrl);
            applicationWindow = fxmlLoader.load();
            shticellController = fxmlLoader.getController();
            shticellController.setMainController(this);
            shticellController.updateUIWithSpreadsheetData(spreadsheetDTO);
            setMainPanelTo(applicationWindow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }
    @Override
    public void close() throws IOException {
        if(dashboardController != null) {
            dashboardController.close();
        }
    }

    public StringProperty currentUserProperty() {
        return currentUser;
    }

    public void updateUserName (String userName){
        currentUser.set(userName);
    }

    public SingleSheetData getSelectedSheet() {
        return null;
    }
}


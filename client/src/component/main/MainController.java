package component.main;

import application.model.DataManager;
import component.dashboard.DashboardController;
import engine.sheetmanager.SheetManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import component.login.LoginController;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static constants.Constants.*;

public class MainController implements Closeable {

    private SheetManager engine;
    private DataManager dataManager;
    private Stage stage;

    @FXML private ScrollPane dashboardWindow;
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
            dashboardController.setInActive();
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
            dashboardController.setShticellController(this);
            setMainPanelTo(dashboardWindow);
            stage.setWidth(dashboardWindow.getPrefWidth());
            stage.setHeight(dashboardWindow.getPrefHeight() + 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    @Override
    public void close() throws IOException {
        //shticellController.close();
    }

    public StringProperty currentUserProperty() {
        return currentUser;
    }

    public void updateUserName (String userName){
        currentUser.set(userName);
    }
}


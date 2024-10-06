package component;

import application.model.DataManager;
import engine.engineimpl.Engine;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import login.LoginController;

public class ShticellController {

    private Engine engine;

    private DataManager dataManager;

    @FXML
    private GridPane loginWindow;

    @FXML
    private LoginController loginController;

    public ShticellController() {

    }
}

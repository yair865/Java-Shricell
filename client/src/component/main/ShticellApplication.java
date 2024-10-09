package component.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

import static constants.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static constants.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class ShticellApplication extends Application {

   @Override
    public void start(Stage stage) throws Exception {

       URL mainPageUrl = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
       FXMLLoader fxmlLoader = new FXMLLoader();
       fxmlLoader.setLocation(mainPageUrl);
       Parent root = fxmlLoader.load();
       MainController mainController = fxmlLoader.getController();
       mainController.setPrimaryStage(stage);
       Scene scene = new Scene(root,400,300);
       stage.setTitle("Shticell Application");
       stage.setScene(scene);
       stage.show();
   }

    public static void main(String[] args) {
        launch(args);
    }
}

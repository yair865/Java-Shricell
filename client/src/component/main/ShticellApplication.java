package component.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.HttpClientUtil;

import java.net.URL;

import static consts.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class ShticellApplication extends Application {

    MainController mainController;
   @Override
    public void start(Stage stage) throws Exception {
       URL mainPageUrl = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
       FXMLLoader fxmlLoader = new FXMLLoader();
       fxmlLoader.setLocation(mainPageUrl);
       Parent root = fxmlLoader.load();
       mainController = fxmlLoader.getController();
       mainController.setPrimaryStage(stage);
       Scene scene = new Scene(root,400,300);
       stage.setTitle("Shticell Application");
       stage.setScene(scene);
       stage.show();
   }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        mainController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

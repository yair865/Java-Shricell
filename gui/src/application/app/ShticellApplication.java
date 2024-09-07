package application.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ShticellApplication extends Application {

   @Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ShticellApplication.fxml")));
       Scene scene = new Scene(root);
       stage.setTitle("Shticell Application");
       stage.setScene(scene);
       stage.show();
   }

    public static void main(String[] args) {
        launch(args);
    }
}

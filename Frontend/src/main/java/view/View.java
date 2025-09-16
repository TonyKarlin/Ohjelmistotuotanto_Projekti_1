package view;

import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.UserApiClient;

public class View extends Application {

    UserApiClient userApiClient;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setOnCloseRequest((WindowEvent t) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setTitle("Pisscord");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginView.fxml"));
            Parent root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setController(userApiClient = new UserApiClient());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

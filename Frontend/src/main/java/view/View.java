package view;

import java.io.IOException;
import java.util.ResourceBundle;

import controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.UserApiClient;
import utils.LanguageManager;

public class View extends Application {

    UserApiClient userApiClient = new UserApiClient();

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setOnCloseRequest((WindowEvent t) -> {
                Platform.exit();
                System.exit(0);
            });

            // Load resource bundle for localization
            ResourceBundle bundle = ResourceBundle.getBundle("localization.LanguageBundle", LanguageManager.getCurrentLocale());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"), bundle);
            Parent root = fxmlLoader.load();
            LoginController controller = fxmlLoader.getController();
            controller.setController(userApiClient);
            stage.setTitle(LanguageManager.getString("title"));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the FXML layout.", e);
        }
    }
}

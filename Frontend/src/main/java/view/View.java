package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setOnCloseRequest((WindowEvent t) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.setTitle("Pisscord");

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view.fxml"));
            Parent root = fxmlLoader.load();
            // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

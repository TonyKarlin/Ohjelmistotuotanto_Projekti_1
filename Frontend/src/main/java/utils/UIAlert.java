package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Data;

@Data
public class UIAlert {

    private String title;
    private String message;
    private Alert.AlertType type;

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public void showSuccessAlert(String title, String message) {
     Alert alert  = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().add(ButtonType.CLOSE);
        alert.showAndWait();
    }
}

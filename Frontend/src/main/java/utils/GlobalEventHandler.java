package utils;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GlobalEventHandler {

    private GlobalEventHandler() {

        // Private constructor to hide the implicit public one
    }

    private static final EventHandler<KeyEvent> ENTER_HANDLER = event -> {
        Node node = (Node) event.getSource();
        Button btn = (Button) node.getProperties().get("enterButton");
        if (event.getCode() == KeyCode.ENTER && btn != null) {
            btn.fire();
            event.consume();
        }
    };

    private static final EventHandler<KeyEvent> ESC_HANDLER = event -> {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (event.getCode() == KeyCode.ESCAPE && stage.isShowing()) {
            stage.close();
        }
    };

    public static void setButtonEventHandler(Node element, Button btn) {
        element.getProperties().put("enterButton", btn);
        element.removeEventHandler(KeyEvent.KEY_PRESSED, ENTER_HANDLER);
        element.addEventHandler(KeyEvent.KEY_PRESSED, ENTER_HANDLER);
    }

    public static void addExitEventHandler(Node element) {
        element.removeEventHandler(KeyEvent.KEY_PRESSED, ESC_HANDLER);
        element.addEventHandler(KeyEvent.KEY_PRESSED, ESC_HANDLER);
    }

    public static void addObservableEventListener(Node element, Button btn) {
        element.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                setButtonEventHandler(element, btn);
            }
        }));
    }
}

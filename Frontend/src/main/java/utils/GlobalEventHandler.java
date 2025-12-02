package utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GlobalEventHandler {

    private GlobalEventHandler() {

        // Private constructor to hide the implicit public one
    }

    public static void setButtonEventHandler(Node element, Button btn) {
        element.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btn.fire();
                event.consume();
            }
        });
    }

    public static void addObservableEventListener(Node element, Button btn) {
        element.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                setButtonEventHandler(element, btn);
            }
        }));
    }
}

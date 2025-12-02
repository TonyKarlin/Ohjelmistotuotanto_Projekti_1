package utils;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;


class GlobalEventHandlerTest {

    private static final AtomicBoolean started = new AtomicBoolean(false);

    @BeforeAll
    static void initWithPlatformStartup() throws Exception {
        if (!started.getAndSet(true)) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);

            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timed out initializing JavaFX (Platform.startup)");
            }
        }
    }

    @Test
    void setButtonEventHandler() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean fired = new AtomicBoolean(false);

        Platform.runLater(() -> {
            VBox node = new VBox();
            Button btn = new Button("OK");
            btn.setOnAction(e -> fired.set(true));

            GlobalEventHandler.setButtonEventHandler(node, btn);
            KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                    false, false, false, false);
            Event.fireEvent(node, enterEvent);

            latch.countDown();
        });

        assertTrue(latch.await(1, TimeUnit.SECONDS), "FX action timed out");
        assertTrue(fired.get(), "Button should have been fired on ENTER");
    }

    @Test
    void addExitEventHandler() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean closed = new AtomicBoolean(false);

        Platform.runLater(() -> {
            VBox root = new VBox();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            GlobalEventHandler.addExitEventHandler(root);
            KeyEvent escEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE,
                    false, false, false, false);
            Event.fireEvent(root, escEvent);

            Platform.runLater(() -> {
                closed.set(!stage.isShowing());
                latch.countDown();
            });
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS), "FX action timed out");
        assertTrue(closed.get(), "Stage should be closed on ESC");
    }

    @Test
    void addObservableEventListener() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean fired = new AtomicBoolean(false);

        Platform.runLater(() -> {
            VBox node = new VBox();
            node.setFocusTraversable(true);
            Button btn = new Button("OK");
            btn.setOnAction(e -> fired.set(true));
            node.getChildren().add(new Button("child"));

            Stage stage = new Stage();
            stage.setScene(new Scene(node));
            stage.show();

            GlobalEventHandler.addObservableEventListener(node, btn);

            node.requestFocus();

            Platform.runLater(() -> {
                KeyEvent enterEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                        false, false, false, false);
                Event.fireEvent(node, enterEvent);
                stage.close();
                latch.countDown();
            });
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "FX action timed out");
        assertTrue(fired.get(), "Button should have been fired after focus + ENTER");
    }
}
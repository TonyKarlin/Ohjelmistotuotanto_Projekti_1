package utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

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
    void addObservableEventListener() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            VBox node = new VBox();
            node.setFocusTraversable(true);

            Button btn = new Button("OK");
            btn.setOnAction(e -> new AtomicBoolean(true));
            node.getChildren().add(new Button("child"));

            Stage stage = new Stage();
            Scene scene = new Scene(node);
            stage.setScene(scene);
            stage.show();

            GlobalEventHandler.addObservableEventListener(node, btn);

            waitForFxEvents();
            node.requestFocus();
            waitFor(node::isFocused);

            Platform.runLater(() -> {
                Robot robot = new Robot();
                robot.keyPress(KeyCode.ENTER);
                robot.keyRelease(KeyCode.ENTER);

                latch.countDown();
            });
        });
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

    private static void waitForFxEvents() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(latch::countDown);
        try {
            latch.await(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private static void waitFor(BooleanSupplier condition) {
        for (int i = 0; i < 20; i++) {
            if (condition.getAsBoolean()) {
                return;
            }
            waitForFxEvents();
        }
        throw new RuntimeException("Condition not met in time");
    }
}

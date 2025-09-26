package utils;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class ImageRounder {
    StackPane container;
    ImageView imageView;


    public ImageRounder(StackPane container, ImageView imageView) {
        this.container = container;
        this.imageView = imageView;
        makeImageRound();
    }

    public void makeImageRound() {
        double radius = container.getPrefWidth() / 2;
        Circle clip = new Circle(radius, radius, radius);
        clip.centerXProperty().bind(container.widthProperty().divide(2));
        clip.centerYProperty().bind(container.heightProperty().divide(2));
        clip.radiusProperty().bind(container.widthProperty().divide(2));
        imageView.setClip(clip);
    }
}

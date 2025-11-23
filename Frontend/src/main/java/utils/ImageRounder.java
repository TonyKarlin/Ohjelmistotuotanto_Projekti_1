package utils;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ImageRounder {

    ImageView imageView;

    public ImageRounder(ImageView imageView) {
        this.imageView = imageView;
        makeImageRound();
    }

    public void makeImageRound() {
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(radius, radius, radius);
        clip.centerXProperty().bind(imageView.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(imageView.fitHeightProperty().divide(2));
        imageView.setClip(clip);
    }
}

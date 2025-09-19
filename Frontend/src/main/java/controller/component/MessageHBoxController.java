package controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import model.Message;

@Data
public class MessageHBoxController {

    Message message;

    public void setController(Message message) {
        this.message = message;
    }

    int id;
    Image image;
    Label text;
    Label createdAt;

    @FXML
    private Label messageLabel;

    @FXML
    private Label messageTimeLabel;

    @FXML
    private Label senderUsernameLabel;

    @FXML
    private ImageView userProfilePicture;

    public MessageHBoxController() {

    }

    public void setText(String text) {
        messageLabel.setText(text);
    }

    public void setTime(String createdAt) {
        messageTimeLabel.setText(createdAt);
    }

    public void setSenderUsername(String senderUsername) {
        senderUsernameLabel.setText(senderUsername);
    }

}

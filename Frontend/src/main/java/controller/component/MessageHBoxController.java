package controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import model.Message;

@Data
public class MessageHBoxController {
    int id;

    Message message;

    public void setController(Message message) {
        this.message = message;
    }


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

    public void setMessageInformation(String text,String createdAt, String senderUsername) {
        messageLabel.setText(text);
        messageTimeLabel.setText(createdAt);
        senderUsernameLabel.setText(senderUsername);
    }

}

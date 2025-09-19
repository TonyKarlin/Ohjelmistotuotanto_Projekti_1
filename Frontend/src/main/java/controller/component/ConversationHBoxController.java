package controller.component;

import controller.ChatDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.Data;
import model.Conversation;

import java.io.IOException;


@Data
public class ConversationHBoxController {

    Conversation conversation;
    ChatDashboardController parentController;

    public void setController(Conversation conversation, ChatDashboardController parentController) {
        this.conversation = conversation;
        this.parentController = parentController;
    }


    @FXML
    private ImageView contactUserPicture;

    @FXML
    private Circle contactUserStatus;

    @FXML
    private Label contactUsername;


    public void setUsername(String username) {
        contactUsername.setText(username);
    }

    public void setUserImage(Image image) {
        contactUserPicture.setImage(image);
    }


    @FXML
    public void openMessages() throws IOException, InterruptedException {
        parentController.showConversationMessages(conversation);
    }
}

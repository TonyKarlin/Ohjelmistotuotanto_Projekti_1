package controller.component;

import controller.ChatDashboardController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Data;
import model.Message;
import request.MessageRequest;
import service.MessageApiClient;

import java.io.IOException;

@Data
public class MessageHBoxController {
    private int id;
    private int senderId;
    private int conversationId;
    private ChatDashboardController parentController;

    Message message;

    public MessageHBoxController() {}

    public void setController(Message message, ChatDashboardController parentController) {
        this.message = message;
        this.parentController = parentController;
    }


    @FXML
    private Label messageLabel;

    @FXML
    private Label messageTimeLabel;

    @FXML
    private Label senderUsernameLabel;

    @FXML
    private ImageView userProfilePicture;

    public void setMessageInformation(String text,String createdAt, String senderUsername) {
        messageLabel.setText(text);
        messageTimeLabel.setText(createdAt);
        senderUsernameLabel.setText(senderUsername);
    }

    @FXML
    public void deleteMessage() throws IOException, InterruptedException {
        int loggedInUSerId = parentController.getLoggedInUser().getId();
        MessageRequest request = new MessageRequest(conversationId, loggedInUSerId, message.getId());
        System.out.println(message.getId());
        System.out.println(conversationId);
        System.out.println(loggedInUSerId);
        MessageApiClient client = new MessageApiClient();
        client.deleteMessage(request);
    }

    @FXML
    public void modifyMessage() {

    }
}

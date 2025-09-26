package controller.component;

import controller.ChatDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Data;
import model.Message;
import request.MessageRequest;
import service.MessageApiClient;
import utils.ImageRounder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;

@Data
public class MessageHBoxController {
    private int id;
    private int senderId;
    private int conversationId;
    private ChatDashboardController parentController;
    private ImageRounder imagerounder;

    private Message message;

    public MessageHBoxController() {}

    public void setController(Message message, ChatDashboardController parentController) {
        this.message = message;
        this.parentController = parentController;
        imagerounder = new ImageRounder(userProfilePicture);
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
        senderUsernameLabel.setText(senderUsername);
        String formattedTime = formatTime(createdAt);
        messageTimeLabel.setText(formattedTime);

    }

    public String formatTime(String createdAt) {
        LocalDateTime dateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        return dateTime.format(outputFormatter);
    }

    @FXML
    public void deleteMessage() throws IOException, InterruptedException {
        int loggedInUSerId = parentController.getLoggedInUser().getId();
        MessageRequest request = new MessageRequest(conversationId, loggedInUSerId, message.getId());
        MessageApiClient client = new MessageApiClient();
        client.deleteMessage(request);
    }

    @FXML
    public void modifyMessage() {

    }
}

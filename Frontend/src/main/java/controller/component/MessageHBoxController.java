package controller.component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import controller.ConversationSettingsController;
import controller.MainViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import lombok.Data;
import model.Conversation;
import model.Message;
import request.MessageRequest;
import service.MessageApiClient;
import utils.ImageRounder;

@Data
public class MessageHBoxController {


    private int conversationId;
    private MainViewController parentController;
    private Message message;
    private static final Logger logger = Logger.getLogger(MessageHBoxController.class.getName());

    public void setController(Message message, MainViewController parentController, Conversation conversation) {
        this.message = message;
        this.parentController = parentController;
        this.conversationId = conversation.getId();
        setMessageInformation(message.getText(), message.getCreatedAt(), message.getSenderUsername());
        setTextInModifyField();
    }

    //region FXML-injected UI component
    @FXML
    private MenuItem deleteMenuitem;

    @FXML
    private TextField editTextField;

    @FXML
    private Label messageLabel;

    @FXML
    private MenuButton messageOptionButton;

    @FXML
    private Label messageTimeLabel;

    @FXML
    private MenuItem modifyMenuItem;

    @FXML
    private HBox rootHbox;

    @FXML
    private Label senderUsernameLabel;

    @FXML
    private ImageView userProfilePicture;
    //endregion

    @FXML
    private void showMenuOption() {
        messageOptionButton.setVisible(message.getSenderId() == parentController.getLoggedInUser().getId());
    }

    @FXML
    private void hideMenuOption() {
        if (!messageOptionButton.isShowing()) {
            messageOptionButton.setVisible(false);
        }
    }

    public void setTextInModifyField() {
        String text = message.getText();
        editTextField.setText(text);
    }

    public void setMessageInformation(String text, String createdAt, String senderUsername) {
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
    public void deleteMessage() throws IOException {
        String token = parentController.getLoggedInUser().getToken();
        MessageApiClient client = new MessageApiClient();
        boolean success = client.deleteMessage(conversationId, message.getId(), token);
        if (success) {
            parentController.deleteMessageLocally(message);
        } else {
            logger.info("Message deletion failed");
        }
    }

    @FXML
    public void modifyMessage() throws IOException, InterruptedException {
        String token = parentController.getLoggedInUser().getToken();
        String modifiedText = editTextField.getText();
        MessageRequest request = new MessageRequest(modifiedText, conversationId, token);
        MessageApiClient client = new MessageApiClient();
        Message updatedMessage = client.modifyMessage(request, message.getId());
        if (updatedMessage != null) {
            this.message = updatedMessage;
            setMessageInformation(message.getText(), message.getCreatedAt(), message.getSenderUsername());
        } else {
            logger.info("Message modification failed on server");
        }

    }
}

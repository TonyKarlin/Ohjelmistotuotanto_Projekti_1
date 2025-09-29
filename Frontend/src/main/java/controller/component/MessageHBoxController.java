package controller.component;

import controller.ChatDashboardController;
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
    private Conversation conversation;

    public MessageHBoxController() {
    }

    public void setController(Message message, ChatDashboardController parentController, Conversation conversation) {
        this.message = message;
        this.parentController = parentController;
        this.conversation = conversation;
        this.id = message.getId();
        this.senderId = message.getSenderId();
        this.conversationId = conversation.getId();
        imagerounder = new ImageRounder(userProfilePicture);
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
        messageOptionButton.setVisible(true);

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
    public void deleteMessage() throws IOException, InterruptedException {
        String token = parentController.getLoggedInUser().getToken();
        MessageApiClient client = new MessageApiClient();
        client.deleteMessage(conversationId,message.getId(),token );
    }


    @FXML
    public void modifyMessage() throws IOException, InterruptedException {
        String token = parentController.getLoggedInUser().getToken();
        String modifiedText = editTextField.getText();
        MessageRequest request = new MessageRequest(modifiedText, conversationId, token);
        MessageApiClient client = new MessageApiClient();
        client.modifyMessage(request, message.getId());
    }
}

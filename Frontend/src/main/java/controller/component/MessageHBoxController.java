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

    public MessageHBoxController() {
    }

    public void setController(Message message, ChatDashboardController parentController) {
        this.message = message;
        this.parentController = parentController;
        imagerounder = new ImageRounder(userProfilePicture);
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
        MessageRequest deleteRequest = new MessageRequest(conversationId,message.getId(), token);
        MessageApiClient client = new MessageApiClient();
        client.deleteMessage(deleteRequest);
    }


    @FXML
    public void modifyMessage() throws IOException, InterruptedException {
        String token = parentController.getLoggedInUser().getToken();
        String modifiedText = editTextField.getText();
        MessageRequest request = new MessageRequest(conversationId, modifiedText, message.getId(), token);
        MessageApiClient client = new MessageApiClient();
        client.modifyMessage(request);
    }
}

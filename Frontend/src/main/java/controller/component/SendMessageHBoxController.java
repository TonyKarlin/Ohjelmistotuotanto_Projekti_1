package controller.component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Conversation;
import model.User;
import request.MessageRequest;
import service.MessageApiClient;

public class SendMessageHBoxController {
        Conversation conversation;
        User loggedInUser;

    public void setController(Conversation conversation, User loggedInUser) {
        this.conversation = conversation;
        this.loggedInUser = loggedInUser;
    }

    @FXML
    private Button messageOptionsButton;

    @FXML
    private Button sendMessageButton;


    @FXML
    private TextField sendMessageTextField;

    @FXML
    public void sendMessageToConversation(ActionEvent event) {
        String message = sendMessageTextField.getText();
        MessageRequest messageRequest = new MessageRequest(message, loggedInUser.getId(), conversation.getId());
        MessageApiClient messageApiClient = new MessageApiClient();
        messageApiClient.sendMessage(messageRequest);


    }


}



package controller.component;

import java.io.IOException;

import controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Conversation;
import model.Message;
import model.User;
import request.MessageRequest;
import service.MessageApiClient;
import utils.GlobalEventHandler;

public class SendMessageHBoxController {

    Conversation conversation;
    User loggedInUser;
    Message message;
    MainViewController parentController;

    public void setController(Conversation conversation, User loggedInUser, MainViewController parentController) {
        this.conversation = conversation;
        this.loggedInUser = loggedInUser;
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        GlobalEventHandler.setButtonEventHandler(sendMessageTextField, sendMessageButton);
    }

    @FXML
    private Button messageOptionsButton;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField sendMessageTextField;

    @FXML
    public void sendMessageToConversation(ActionEvent event) throws IOException {
        message = new Message();
        String sentMessage = sendMessageTextField.getText();
        if (sentMessage.isEmpty()) {
            return;
        }
        MessageRequest messageRequest = new MessageRequest(sentMessage, conversation.getId(), loggedInUser.getToken());
        MessageApiClient messageApiClient = new MessageApiClient();
        Message sendedMessage = messageApiClient.sendMessage(messageRequest);
        parentController.addMessageToConversation(sendedMessage, conversation);
        sendMessageTextField.clear();

    }



}

package controller;

import controller.component.ConversationHBoxController;
import controller.component.ConversationParticipantHBoxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

import java.io.IOException;

public class ConversationSettingsController {
    ConversationRequest conversationRequest;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    User loggedInuser;
    Conversation Updatedconversation;
    Conversation conversation;
    ChatDashboardController parentController;
    ConversationHBoxController conversationHBoxController;

    public void setController(User loggedInuser, Conversation conversation,
                              ChatDashboardController parentController,
                              ConversationHBoxController conversationHBoxController) throws IOException {

        this.loggedInuser = loggedInuser;
        this.conversation = conversation;
        this.parentController = parentController;
        this.conversationHBoxController = conversationHBoxController;
        addConversationParticipants();
    }


    @FXML
    private Button ChangeNameButton;

    @FXML
    private ListView<HBox> conversationParticipantList;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button deleteButton;

    @FXML
    void changeConversationName(ActionEvent event) throws IOException, InterruptedException {
        String conversationName = nameTextField.getText();
        conversationRequest = new ConversationRequest(conversationName, conversation.getId(), loggedInuser.getToken());
        Updatedconversation = conversationApiClient.changeConversationName(conversationRequest);
        if (Updatedconversation != null) {
            conversationHBoxController.setConversationInformation(Updatedconversation);
            this.conversation = Updatedconversation;
        }else {
            System.out.println("failed to change conversation name");
        }
    }

    @FXML
    public void deleteConversation() throws IOException, InterruptedException {
        boolean success = conversationApiClient.deleteConversation(conversation, loggedInuser);
        if (success) {
            parentController.conversations.remove(conversation);
            parentController.addConversation();
        } else {
            System.out.println("Deletion failed");
        }
    }

    public void addConversationParticipants() throws IOException {
        conversationParticipantList.getItems().clear();
        for (ConversationParticipant p: conversation.getParticipants()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/conversationParticipantHBox.fxml"));
            HBox conversationParticipantHBox = fxmlLoader.load();
            ConversationParticipantHBoxController controller = fxmlLoader.getController();
            controller.setController(p, this.conversation, loggedInuser, conversationParticipantHBox, conversationParticipantList);
            conversationParticipantList.getItems().add(conversationParticipantHBox);
        }
    }
}


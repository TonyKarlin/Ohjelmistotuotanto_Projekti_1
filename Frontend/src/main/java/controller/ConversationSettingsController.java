package controller;

import controller.component.ContactHboxController;
import controller.component.ConversationHBoxController;
import controller.component.ConversationParticipantHBoxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Contact;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConversationSettingsController {
    ConversationRequest conversationRequest;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    User loggedInuser;
    Conversation Updatedconversation;
    Conversation conversation;
    ChatDashboardController parentController;
    ConversationHBoxController conversationHBoxController;
    List<Contact> contacts;

    public void setController(User loggedInuser, Conversation conversation,
                              ChatDashboardController parentController,
                              ConversationHBoxController conversationHBoxController, List<Contact> contacts) throws IOException {

        this.loggedInuser = loggedInuser;
        this.conversation = conversation;
        this.parentController = parentController;
        this.conversationHBoxController = conversationHBoxController;
        this.contacts = contacts;
        checkOwnerPermissions();
        showParticipantsInAGroup();
        setConversationName(conversation.getName());
    }


    //region
    @FXML
    private MenuButton optionButton;
    @FXML
    private MenuItem leaveMenuItem;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private Button ChangeNameButton;
    @FXML
    private Button addFriendButton;
    @FXML
    private Button showParticipantButton;
    @FXML
    private ListView<HBox> conversationParticipantList;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Label conversationNameLabel;
    //endregion

    public void setConversationName(String name) {
        conversationNameLabel.setText(name);
    }


    @FXML
    void changeConversationName(ActionEvent event) throws IOException, InterruptedException {
        String conversationName = nameTextField.getText();
        conversationRequest = new ConversationRequest(conversationName, conversation.getId(), loggedInuser.getToken());
        Updatedconversation = conversationApiClient.changeConversationName(conversationRequest);
        if (Updatedconversation != null) {
            conversationHBoxController.setConversationInformation(Updatedconversation);
            this.conversation = Updatedconversation;
            setConversationName(conversation.getName());
        } else {
            System.out.println("failed to change conversation name");
        }
    }

    @FXML
    public void deleteConversation() throws IOException, InterruptedException {
        boolean success = conversationApiClient.deleteConversation(conversation, loggedInuser);
        if (success) {
            parentController.conversations.remove(conversation);
            parentController.addConversations(conversation.getType());
        } else {
            System.out.println("Deletion failed");
        }
    }

    @FXML
    public void leaveFromConversation() {

    }

    @FXML
    public void showParticipantsInAGroup() throws IOException {
        conversationParticipantList.getItems().clear();
        for (ConversationParticipant p : conversation.getParticipants()) {
            if (p.getUserId() == loggedInuser.getId()) {
                break;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/conversationParticipantHBox.fxml"));
            HBox participantHBox = fxmlLoader.load();
            ConversationParticipantHBoxController controller = fxmlLoader.getController();
            controller.setController(p, conversation, loggedInuser, participantHBox, conversationParticipantList);
            conversationParticipantList.getItems().add(participantHBox);
        }

    }

    @FXML
    public void showFriendsToAdd() throws IOException {
        conversationParticipantList.getItems().clear();
        for (Contact c : contacts) {
            boolean isInConversation = false;
            for (ConversationParticipant p : conversation.getParticipants()) {
                if (p.getUserId() == c.getContactUserId()) {
                    isInConversation = true;
                    break;
                }
            }
            if (isInConversation) continue;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/contactHBox.fxml"));
            HBox contactHBox = fxmlLoader.load();
            ContactHboxController controller = fxmlLoader.getController();
            controller.setController(c, parentController);
            controller.setController(conversation, loggedInuser);
            controller.setUsername(c.getContactUsername());
            conversationParticipantList.getItems().add(contactHBox);
        }
    }

    public void checkOwnerPermissions() {
        boolean isOwner = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUserId() == loggedInuser.getId() && "OWNER".equals(p.getRole()));
        deleteMenuItem.setVisible(isOwner);
    }


}



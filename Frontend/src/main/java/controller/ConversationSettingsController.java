package controller;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.component.ContactHboxController;
import controller.component.ConversationHBoxController;
import controller.component.ConversationParticipantHBoxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Contact;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;
import utils.LanguageManager;
import utils.UIAlert;

public class ConversationSettingsController {

    ConversationRequest conversationRequest;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    User loggedInuser;
    Conversation updatedconversation;
    Conversation conversation;
    MainViewController parentController;
    ConversationHBoxController conversationHBoxController;
    List<Contact> contacts;
    UIAlert alert = new UIAlert();
    private static final Logger logger = Logger.getLogger(ConversationSettingsController.class.getName());

    public void setController(User loggedInuser, Conversation conversation,
                              MainViewController parentController,
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
    private Button changeNameButton;
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
    void changeConversationName(ActionEvent event) throws IOException {
        String conversationName = nameTextField.getText();
        conversationRequest = new ConversationRequest(conversationName, conversation.getId(), loggedInuser.getToken());
        updatedconversation = conversationApiClient.changeConversationName(conversationRequest);
        if (updatedconversation != null) {
            conversationHBoxController.setConversationInformation(updatedconversation);
            this.conversation = updatedconversation;
            setConversationName(conversation.getName());
        } else {
            logger.log(Level.WARNING, "Failed to change conversation name");
        }
    }

    @FXML
    public void deleteConversation() throws IOException {
        boolean success = conversationApiClient.deleteConversation(conversation, loggedInuser);
        if (success) {
            parentController.conversations.remove(conversation);
            parentController.addConversations(conversation.getType());
        } else {
            logger.log(Level.WARNING, "Deletion failed");
        }
    }

    @FXML
    public void leaveFromConversation() {
        //to be implemented
    }

    @FXML
    public void showParticipantsInAGroup() throws IOException {
        conversationParticipantList.getItems().clear();
        for (ConversationParticipant p : conversation.getParticipants()) {
            if (p.getUserId() == loggedInuser.getId()) {
                continue; // skip yourself

            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/conversationParticipantHBox.fxml"));
            ResourceBundle bundle = ResourceBundle.getBundle("localization.LanguageBundle", LanguageManager.getCurrentLocale());
            fxmlLoader.setResources(bundle);
            HBox participantHBox = fxmlLoader.load();
            ConversationParticipantHBoxController controller = fxmlLoader.getController();
            controller.setController(p, conversation, loggedInuser, participantHBox, conversationParticipantList);
            conversationParticipantList.getItems().add(participantHBox);
        }

    }


    public boolean isInConversation(Contact contact) {
        return conversation.getParticipants().stream()
                .anyMatch(p -> p.getUserId() == contact.getContactUserId());
    }


    public void showFriendsToAdd() throws IOException {
        conversationParticipantList.getItems().clear();
        for (Contact c : contacts) {

            boolean shouldSkip =
                    c.getContactUserId() == loggedInuser.getId() ||
                            isInConversation(c);

            if (shouldSkip) {
                continue;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/contactHBox.fxml"));
            ResourceBundle bundle = ResourceBundle.getBundle("localization.LanguageBundle", LanguageManager.getCurrentLocale());
            fxmlLoader.setResources(bundle);
            HBox contactHBox = fxmlLoader.load();
            ContactHboxController controller = fxmlLoader.getController();
            controller.setController(c, parentController);
            controller.setControllerForConversationSettings(conversation, loggedInuser, this);
            controller.setUsername(c.getContactUsername());
            conversationParticipantList.getItems().add(contactHBox);
        }
    }

    public void addUserToConversation(Contact contact, ContactHboxController hboxController) throws IOException, InterruptedException {
        boolean success = conversationApiClient.addUserToConversation(
                conversation.getId(), contact.getContactUserId(), loggedInuser.getToken()
        );
        if (success) {
            hboxController.getAddButton().setVisible(false);
            alert.showSuccessAlert(LanguageManager.getString("register_success_title"), LanguageManager.getString("user_added_successfully") + conversation.getName());
            conversation.getParticipants().add(new ConversationParticipant(contact.getContactUserId(), contact.getContactUsername(), "MEMBER"));
        } else {
            alert.showErrorAlert(LanguageManager.getString("failed"), LanguageManager.getString("user_added_failed"));
        }
    }

    public void checkOwnerPermissions() {
        boolean isOwner = conversation.getParticipants().stream()
                .anyMatch(p -> p.getUserId() == loggedInuser.getId() && "OWNER".equals(p.getRole()));
        deleteMenuItem.setVisible(isOwner);
    }

}

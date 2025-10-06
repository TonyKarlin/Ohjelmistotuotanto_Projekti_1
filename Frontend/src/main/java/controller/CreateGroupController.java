package controller;

import controller.component.ContactHboxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.Contact;
import model.Conversation;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupController {

    private List<Contact> contacts;
    private List<Integer> selectedContacts = new ArrayList<>();
    private ConversationApiClient conversationApiClient;
    private ConversationRequest request;
    private User loggedInUser;
    private ChatDashboardController parentController;


    public void setController(List<Contact> contacts, User loggedInUser, ChatDashboardController parentController) throws IOException {
        this.contacts = contacts;
        this.loggedInUser = loggedInUser;
        this.parentController = parentController;
        addContactsToList();
    }

    @FXML
    private Button CreateButton;

    @FXML
    private ListView<HBox> contactList;

    @FXML
    private TextField nameTextField;

    @FXML
    public void createConversation(ActionEvent event) throws IOException, InterruptedException {
        conversationApiClient = new ConversationApiClient();
        String name = nameTextField.getText();
        request = new ConversationRequest(loggedInUser.getId(), name, selectedContacts);
        Conversation newConversation = conversationApiClient.createConversation(request, loggedInUser.getToken());
        if (newConversation != null) {
            parentController.getConversations().add(newConversation);
            parentController.addConversations("GROUP");
        }
    }


    public void addContactsToList() throws IOException {
        for (Contact c : contacts) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/contactHBox.fxml"));
            HBox contactHBox = loader.load();
            ContactHboxController controller = loader.getController();
            controller.setControllerForCreateGroup(this, c);
            contactList.getItems().add(contactHBox);
        }

    }

    public void handleSelectContact(Contact contact, ContactHboxController controller) {
        if (!selectedContacts.contains(contact.getContactUserId())) {
            selectedContacts.add(contact.getContactUserId());
            controller.getAddButton().setText("Selected");
        } else {
            selectedContacts.remove(contact.getContactUserId());
            controller.getAddButton().setText("Add");
        }
    }

}

package controller;

import java.io.IOException;
import java.util.List;

import callback.ContactUpdateCallback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Contact;
import model.User;
import request.ContactRequest;
import service.ContactApiClient;
import service.UserApiClient;
import utils.GlobalEventHandler;
import utils.LanguageManager;
import utils.UIAlert;

public class AddFriendsController {

    User loggedInuser;
    UserApiClient userApiClient;
    ContactApiClient contactApiClient = new ContactApiClient();
    private final UIAlert alert = new UIAlert();
    private List<Contact> contacts;
    private ContactUpdateCallback contactUpdateCallback;

    public void setController(User loggedInuser,
            UserApiClient userApiClient,
            List<Contact> contacts,
            ContactUpdateCallback contactUpdateCallback) {

        this.loggedInuser = loggedInuser;
        this.userApiClient = userApiClient;
        this.contacts = contacts;
        this.contactUpdateCallback = contactUpdateCallback;
    }

    @FXML
    public void initialize() {
        GlobalEventHandler.setButtonEventHandler(searchFriendTextField, friendRequestButton);
    }

    @FXML
    private Button friendRequestButton;

    @FXML
    private TextField searchFriendTextField;

    @FXML
    private Button closeButton;

    @FXML
    public void sendFriendRequest(ActionEvent event) throws InterruptedException{

        String username = searchFriendTextField.getText().trim();

        if (username.isEmpty()) {
            alert.showErrorAlert(LanguageManager.getString("empty_field"), username);
            return;
        }

        if (loggedInuser.getUsername().equals(username)) {
            alert.showErrorAlert(LanguageManager.getString("add_self"), username);
            return;
        }

        try {

            // check if the contact is already in your contacts PENDING or ACCEPTED
            Contact foundContact = null;
            for (Contact contact : contacts) {
                if (username.equals(contact.getContactUsername())) {
                    foundContact = contact;
                    break;
                }
            }

            if (foundContact != null) {
                // If the contact is PENDING
                if ("PENDING".equals(foundContact.getStatus())) {
                    // If the current user sent the friend request
                    if (foundContact.getInitiatorId() == loggedInuser.getId()) {
                        alert.showErrorAlert(LanguageManager.getString("already_sent") + foundContact.getContactUsername(), username);
                        return;

                    } else {
                        // else accept the request
                        contactApiClient.acceptContact(new ContactRequest(loggedInuser.getId(), foundContact.getContactUserId(), loggedInuser.getToken()));
                        alert.showSuccessAlert(LanguageManager.getString("request_from") + foundContact.getContactUsername() + LanguageManager.getString("accepted"), username);
                        // Lastly update contacts
                        updateContactsList(loggedInuser);
                        return;
                    }
                }
                alert.showErrorAlert(LanguageManager.getString("already_friends") + foundContact.getContactUsername(), username);
                return;

            }
            // Otherwise check if the user exists
            User foundUser = userApiClient.getUserByUsername(username, loggedInuser.getToken());
            if (foundUser == null) {
                alert.showErrorAlert(LanguageManager.getString("user_not_found"), username);
                return;
            }

            // Send a friend request to the found user
            contactApiClient.addContact(new ContactRequest(loggedInuser.getId(), foundUser.getId(), loggedInuser.getToken()));
            alert.showSuccessAlert(LanguageManager.getString("request_sent") + foundUser.getUsername(), username);

            // Lastly update contacts
            updateContactsList(loggedInuser);

        } catch (IOException e) {
            alert.showErrorAlert(LanguageManager.getString("error_sending") + e.getMessage(), username);
        }
    }

    public void updateContactsList(User loggedInUser) throws IOException, InterruptedException {
        contacts = contactApiClient.getAllUserContacts(loggedInUser);

        // Notify the parent controller about the updated contacts
        if (contactUpdateCallback != null) {
            contactUpdateCallback.onContactsUpdated(contacts);
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}

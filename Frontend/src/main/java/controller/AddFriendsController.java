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
            ContactApiClient contactApiClient,
            List<Contact> contacts,
            ContactUpdateCallback contactUpdateCallback) {

        this.loggedInuser = loggedInuser;
        this.userApiClient = userApiClient;
        this.contacts = contacts;
        this.contactUpdateCallback = contactUpdateCallback;
    }

    @FXML
    private Button friendRequestButton;

    @FXML
    private TextField searchFriendTextField;

    @FXML
    private Button closeButton;

    @FXML
    public void sendFriendRequest(ActionEvent event) {

        String username = searchFriendTextField.getText().trim();

        if (username.isEmpty()) {
            alert.showErrorAlert("Field is empty", username);
            return;
        }

        if (loggedInuser.getUsername().equals(username)) {
            alert.showErrorAlert("Can't add yourself", username);
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
                        alert.showErrorAlert("Friend request already sent to: " + foundContact.getContactUsername(), username);
                        return;

                    } else {
                        // else accept the request
                        contactApiClient.acceptContact(new ContactRequest(loggedInuser.getId(), foundContact.getContactUserId(), loggedInuser.getToken()));
                        alert.showSuccessAlert("Friend request from " + foundContact.getContactUsername() + " accepted!", username);
                        // Lastly update contacts
                        updateContactsList(loggedInuser);
                        return;
                    }
                }
                alert.showErrorAlert("You're already friends with: " + foundContact.getContactUsername(), username);
                return;

            }
            // Otherwise check if the user exists
            User foundUser = userApiClient.getUserByUsername(username, loggedInuser.getToken());
            if (foundUser == null) {
                alert.showErrorAlert("User not found", username);
                return;
            }

            // Send a friend request to the found user
            contactApiClient.addContact(new ContactRequest(loggedInuser.getId(), foundUser.getId(), loggedInuser.getToken()));
            alert.showSuccessAlert("Friend request sent to: " + foundUser.getUsername(), username);

            // Lastly update contacts
            updateContactsList(loggedInuser);

        } catch (IOException | InterruptedException e) {
            alert.showErrorAlert("Error sending friend request: " + e.getMessage(), username);
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

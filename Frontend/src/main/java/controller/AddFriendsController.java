package controller;

import java.io.IOException;
import java.util.List;

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
    private List<Contact> pendingContacts;
    private List<Contact> sentContacts;

    public void setController(User loggedInuser,
            UserApiClient userApiClient,
            ContactApiClient contactApiClient,
            List<Contact> contacts,
            List<Contact> pendingContacts,
            List<Contact> sentContacts) {

        this.loggedInuser = loggedInuser;
        this.userApiClient = userApiClient;
        this.contacts = contacts;
        this.pendingContacts = pendingContacts;
        this.sentContacts = sentContacts;
    }

    @FXML
    private Button friendRequestButton;

    @FXML
    private TextField searchFriendTextField;

    @FXML
    private Button closeButton;

    @FXML
    void sendFriendRequest(ActionEvent event) {

        String username = searchFriendTextField.getText();

        if ("".equals(username)) {
            alert.showErrorAlert("Field is empty", username);
            return;
        }

        if (loggedInuser.getUsername().equals(username)) {
            alert.showErrorAlert("Cant add yourself", username);
            return;
        }

        if (username != null) {
            try {
                User foundUser = userApiClient.getUserByUsername(username);
                if (foundUser != null) {
                    // Check if already friends/contacts

                    if (isContact(foundUser)) {
                        alert.showErrorAlert("You are already friends with user: " + foundUser.getUsername(), username);
                        return;
                    }

                    ContactRequest contactRequest = new ContactRequest(loggedInuser.getId(), foundUser.getId());

                    if (isSentContact(foundUser)) {
                        alert.showErrorAlert("You've already sent a friend request to user: " + foundUser.getUsername(), username);
                        return;
                    }

                    // if the contact is in your pending contacts list accept it
                    if (isPendingContact(foundUser)) {
                        alert.showSuccessAlert("Friend request sent to user: " + foundUser.getUsername(), username);
                        contactApiClient.acceptContact(contactRequest);
                        return;
                    }

                    alert.showSuccessAlert("Friend request sent to: " + foundUser.getUsername(), username);

                    contactApiClient.addContact(contactRequest);

                } else {
                    System.out.println("User not found");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error fetching user or contacts: " + e.getMessage());
            }
        }

    }

    public boolean isContact(User foundUser) {
        boolean alreadyContact = false;
        if (contacts != null) {
            for (Contact contact : contacts) {
                if (contact.getContactUserId() == foundUser.getId()) {
                    alreadyContact = true;
                    break;
                }
            }
        }
        System.out.println(alreadyContact + " CONTACT");
        return alreadyContact;
    }

    public boolean isPendingContact(User foundUser) {
        boolean alreadyPending = false;
        if (pendingContacts != null) {
            for (Contact contact : pendingContacts) {
                if (contact.getContactUserId() == foundUser.getId()) {
                    System.out.println(contact);
                    alreadyPending = true;
                    break;
                }
            }
        }
        System.out.println(alreadyPending + " PENDING");
        return alreadyPending;
    }

    public boolean isSentContact(User foundUser) {
        boolean alreadySent = false;
        for (Contact contact : sentContacts) {
            System.out.println(contact.getContactUsername());
        }
        if (sentContacts != null) {
            for (Contact contact : sentContacts) {
                if (contact.getContactUserId() == foundUser.getId()) {
                    System.out.println(contact);
                    alreadySent = true;
                    break;
                }
            }
        }
        System.out.println(alreadySent + " SENT");
        return alreadySent;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}

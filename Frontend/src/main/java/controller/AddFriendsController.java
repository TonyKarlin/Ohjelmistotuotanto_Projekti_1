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

public class AddFriendsController {

    User loggedInuser;
    UserApiClient userApiClient;
    ContactApiClient contactApiClient = new ContactApiClient();

    public void setController(User loggedInuser, UserApiClient userApiClient, ContactApiClient contactApiClient) {
        this.loggedInuser = loggedInuser;
        this.userApiClient = userApiClient;
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
            System.out.println("Field is empty");
            return;
        }

        if (username != null) {
            try {
                User foundUser = userApiClient.getUserByUsername(username);
                if (foundUser != null) {
                    // Check if already friends/contacts
                    List<Contact> contacts = contactApiClient.getAllUserContacts(loggedInuser);
                    boolean alreadyContact = false;
                    if (contacts != null) {
                        for (Contact contact : contacts) {
                            if (contact.getContactUserId() == foundUser.getId()) {
                                alreadyContact = true;
                                break;
                            }
                        }
                    }
                    if (alreadyContact) {
                        System.out.println();
                        System.out.println("You are already friends with this user.");
                        return;
                    }
                    // Otherwise, send friend request logic here
                    System.out.println("Friend request sent to: " + foundUser.getUsername());

                    ContactRequest contactRequest = new ContactRequest(loggedInuser.getId(), foundUser.getId());
                    contactApiClient.addContact(contactRequest);

                } else {
                    System.out.println("User not found");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error fetching user or contacts: " + e.getMessage());
            }
        }

    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}

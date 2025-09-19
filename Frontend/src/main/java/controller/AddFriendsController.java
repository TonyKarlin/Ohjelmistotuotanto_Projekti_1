package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.User;
import service.UserApiClient;

public class AddFriendsController {

    User loggedInuser;
    UserApiClient userApiClient;

    @FXML
    private Button friendRequestButton;

    @FXML
    private TextField searchFriendTextField;

    @FXML
    void sendFriendRequest(ActionEvent event) {

    }

    public void setController(User loggedInuser, UserApiClient userApiClient) {
        this.loggedInuser = loggedInuser;
        this.userApiClient = userApiClient;
    }

}

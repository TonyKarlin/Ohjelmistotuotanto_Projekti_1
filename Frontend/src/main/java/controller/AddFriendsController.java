package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.UserApiClient;

public class AddFriendsController {

    User loggedInuser;
    UserApiClient userApiClient;

    public void setController(User loggedInuser, UserApiClient userApiClient) {
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

        if (username != null) {
            System.out.println(userApiClient.getUserByUsername(username));
        }

    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}

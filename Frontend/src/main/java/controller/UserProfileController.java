package controller;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import model.User;
import request.UpdateUserRequest;
import service.UserApiClient;
import utils.UIAlert;

import java.util.Objects;


public class UserProfileController {

    User loggedInUser;
    UserApiClient client;
    UIAlert uiAlert = new UIAlert();


    public void setController(User loggedInUser, UserApiClient client) {
        this.loggedInUser = loggedInUser;
        this.client = client;
        addUserInformation(loggedInUser);
    }

    @FXML
    private Circle awayStatus;

    @FXML
    private Circle userCurrentStatus;

    @FXML
    private Button changeInformationButton;

    @FXML
    private Button changeUserPictureButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private Circle offlineStatus;

    @FXML
    private Circle onlineStatus;

    @FXML
    private PasswordField passwordField;

    @FXML
    private StackPane profilePictureContainer;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private ImageView userProfilePicture;

    @FXML
    private TextField usernameTextField;

    @FXML
    public void changeInformation(ActionEvent event) {
        String newUsername = usernameTextField.getText();
        String newEmail = emailTextField.getText();
        String oldPassword = passwordField.getText();
        String newPassword = repeatPasswordField.getText();

        UpdateUserRequest request = new UpdateUserRequest(newUsername, newEmail,loggedInUser.getUserId(), oldPassword, newPassword);
        this.loggedInUser = client.updateUser(request, loggedInUser);
        System.out.println(loggedInUser.getUsername());


    }

    @FXML
    public void changeUserStatus(MouseEvent event) {
        Circle clickedCircle = (Circle) event.getSource();
        userCurrentStatus.setFill(clickedCircle.getFill());

    }

    public void addUserInformation(User loggedInUser) {
        emailTextField.setText(loggedInUser.getEmail());
        usernameTextField.setText(loggedInUser.getUsername());
    }
}


package controller;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import model.User;
import model.UserResponse;
import request.UpdateUserRequest;
import service.UserApiClient;
import utils.FileHandler;
import utils.ImageRounder;
import utils.UIAlert;

import java.io.File;

public class UserProfileController {

    User loggedInUser;
    UserApiClient client;
    ChatDashboardController parentController;
    UIAlert alert = new UIAlert();
    ImageRounder imageRounder;
    FileHandler fileHandler;
    UpdateUserRequest request;
    UserResponse userResponse;

    public void setController(User loggedInUser, UserApiClient client, ChatDashboardController parentController) {
        this.loggedInUser = loggedInUser;
        this.client = client;
        this.parentController = parentController;
        addUserInformation(loggedInUser);
    }

    //region FXML-injected UI components
    @FXML
    private Circle awayStatus;
    @FXML
    private Circle userCurrentStatus;
    @FXML
    private Button changeInformationButton;
    @FXML
    private Button changeProfilePictureButton;
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
    //endregion

    @FXML
    public void changeUserStatus(MouseEvent event) {
        Circle clickedCircle = (Circle) event.getSource();
        userCurrentStatus.setFill(clickedCircle.getFill());
    }

    @FXML
    public void changeInformation(ActionEvent event) {
        String newUsername = usernameTextField.getText();
        String newEmail = emailTextField.getText();
        String newPassword = passwordField.getText();
        String repeatedPassword = repeatPasswordField.getText();
        if (!checkTextFields(newUsername, newEmail)) {
            return;
        }
        if (!checkPassword(newPassword, repeatedPassword)) {
            return;
        }
        request = new UpdateUserRequest(newUsername, newEmail, newPassword);
        userResponse = new UserResponse();
        userResponse = client.updateUser(request, loggedInUser);
        if (userResponse == null) {
            alert.showErrorAlert("Updated failed", "Failed to update user");
        } else {
            alert.showSuccessAlert("Success", "User updated successfully ✅");
            String token = userResponse.getToken();
            loggedInUser = userResponse.getUser();
            loggedInUser.setToken(token);
            parentController.setLoggedInUser(loggedInUser);
            parentController.setUserInformation();
            addUserInformation(loggedInUser);
        }
    }

    @FXML
    public void changeProfilePicture() {
        fileHandler = new FileHandler();
        File selectedPicture = fileHandler.selectProfilePicture(changeProfilePictureButton.getScene().getWindow());
        if (selectedPicture != null) {
            Image image = new Image(selectedPicture.toURI().toString());
            userProfilePicture.setImage(image);
            userProfilePicture.setPreserveRatio(false);
            request = new UpdateUserRequest(selectedPicture);
            String token = loggedInUser.getToken();
            loggedInUser = client.updateUserProfilePicture(request, loggedInUser);
            loggedInUser.setToken(token);
            parentController.setLoggedInUser(loggedInUser);
            parentController.setUserInformation();

        }
    }

    public boolean checkPassword(String password, String repeatedPassword) {
        if (password.isEmpty() && repeatedPassword.isEmpty()) {
            return true;
        }
        if (!password.equals(repeatedPassword)) {
            alert.showErrorAlert("Error", "passwords don't match");
            return false;
        }
        if (password.length() < 6) {
            alert.showErrorAlert("Invalid Password", "Password should contain 6 or more characters.");
            return false;
        }
        return true;
    }

    // Check the user inputs that hey are valid. Gets alert if something is wrong
    public boolean checkTextFields(String username, String email) {
        if (username.isEmpty() || email.isEmpty()) {
            alert.showErrorAlert("Empty fields", "Please fill in all fields");
            return false;
        } else if (username.length() < 6) {
            alert.showErrorAlert("Invalid Name", "Name should contain 6 or more characters.");
            return false;
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            alert.showErrorAlert("Invalid Email", "Please enter a valid email address.");
            return false;
        }
        return true;
    }

    public void addUserInformation(User loggedInUser) {
        emailTextField.setText(loggedInUser.getEmail());
        usernameTextField.setText(loggedInUser.getUsername());
        Image profilePicture = new Image(loggedInUser.getProfilePictureUrl());
        userProfilePicture.setImage(profilePicture);
        imageRounder = new ImageRounder(userProfilePicture);
        userProfilePicture.setImage(profilePicture);
        userProfilePicture.setPreserveRatio(false);
    }
}


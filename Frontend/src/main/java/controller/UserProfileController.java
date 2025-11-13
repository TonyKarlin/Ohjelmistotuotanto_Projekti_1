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
import utils.LanguageManager;
import utils.UIAlert;

import java.io.File;
import java.io.IOException;

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
    public void changeInformation(ActionEvent event) throws IOException, InterruptedException {
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
            alert.showErrorAlert(LanguageManager.getString("user_updated_failed_title"), LanguageManager.getString("user_updated_failed_message"));
        } else {
            alert.showErrorAlert(LanguageManager.getString("register_success_title"), LanguageManager.getString("user_updated_successfully"));
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
            String token = loggedInUser.getToken();
            loggedInUser = client.updateUserProfilePicture(selectedPicture, loggedInUser);
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
            alert.showErrorAlert(LanguageManager.getString("register_error_title"), LanguageManager.getString("password_missmatch"));
            return false;
        }
        if (password.length() < 6) {
            alert.showErrorAlert(LanguageManager.getString("register_invalid_password_title"),LanguageManager.getString("register_invalid_password"));
            return false;
        }
        return true;
    }

    // Check the user inputs that hey are valid. Gets alert if something is wrong
    public boolean checkTextFields(String username, String email) {
        if (username.isEmpty() || email.isEmpty()) {
            alert.showErrorAlert(LanguageManager.getString("register_empty_fields_title"), LanguageManager.getString("register_empty_fields"));
            return false;
        } else if (username.length() < 6) {
            alert.showErrorAlert(LanguageManager.getString("register_invalid_name_title"), LanguageManager.getString("register_invalid_name"));
            return false;
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            alert.showErrorAlert(LanguageManager.getString("register_invalid_email_title"), LanguageManager.getString("register_invalid_email"));
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


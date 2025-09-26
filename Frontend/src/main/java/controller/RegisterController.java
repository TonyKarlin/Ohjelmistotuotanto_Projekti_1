package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import service.UserApiClient;
import utils.ImageRounder;
import utils.UIAlert;

public class RegisterController {

    private UserApiClient userApiClient;
    private ImageRounder imageRounder;
    private UIAlert alert = new UIAlert();

    //Controller to set instances. Is called when changing to this view.
    public void setController(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;
        imageRounder = new ImageRounder(userProfilePicture);
    }

    @FXML
    private Button addPictureButton;

    @FXML
    private ImageView userProfilePicture;

    @FXML
    private StackPane profilePictureContainer;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label loginLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private TextField usernameTextField;

    public RegisterController() {
    }

    //To move back to login view
    @FXML
    void moveToLoginView(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        // pass the userApiClient instance back to login view
        controller.setController(this.userApiClient);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Check the user inputs that hey are valid. Gets alert if something is wrong
    public boolean checkTextFields(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            alert.showErrorAlert("Empty fields", "Please fill in all fields");
            return false;
        } else if (username.length() < 6) {
            alert.showErrorAlert("Invalid Name", "Name should contain 6 or more characters.");
            return false;
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            alert.showErrorAlert("Invalid Email", "Please enter a valid email address.");
            return false;
        } else if (password.length() < 6) {
            alert.showErrorAlert("Invalid password", "Password should contain 6 or more characters.");
        }

        return true;
    }

    //Check that the password user inputs match with each other
    public boolean checkPassword(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            alert.showErrorAlert("Error", "passwords don't match");
            return false;
        }
        return true;
    }

    //right now change the image in register view but don't send it to the server
    @FXML
    void addProfilePicture(ActionEvent event) {
        // Open file explorer and shows only images
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("image Files",
                        "*.png", "*.jpg", ".jpeg")
        );
        // when image is selected from file explorer change it in the register view and make it also a round
        File selectedPicture = fileChooser.showOpenDialog(addPictureButton.getScene().getWindow());
        if (selectedPicture != null) {
            Image image = new Image(selectedPicture.toURI().toString());
            userProfilePicture.setImage(image);
            userProfilePicture.setFitWidth(profilePictureContainer.getPrefWidth());
            userProfilePicture.setFitHeight(profilePictureContainer.getPrefHeight());
            userProfilePicture.setPreserveRatio(false);
        }
    }

    //Register user to db
    @FXML
    void registerUser() {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatPasswordField.getText();
        String profilePicture = String.valueOf(userProfilePicture);
        if (!checkTextFields(username, email, password)) {
            return;
        }
        if (!checkPassword(password, repeatedPassword)) {
            return;
        }
        try {
            //creates user object from the user inputs
            User user = new User(username, email, password, profilePicture);
            //Sends the user object to the server and creates another user from the backend response
            User checkIfUserExist = userApiClient.registerUser(user);
            //If response is not user information but response message, user is null so send this alert message
            if (checkIfUserExist == null) {
                alert.showErrorAlert("Existing User", "User already exists");
            } else {
                // Registration successful, move to login view
                try {
                    alert.showSuccessAlert("Success", "User created successfully ✅");
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
                    Parent root = fxmlLoader.load();
                    LoginController controller = fxmlLoader.getController();
                    controller.setController(this.userApiClient);
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    alert.showErrorAlert("Error", "Could not load login view.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

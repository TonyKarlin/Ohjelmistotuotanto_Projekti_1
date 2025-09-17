package controller;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.User;
import service.UserApiClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class RegisterController {

    UserApiClient userApiClient;

    public void setController(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;
        makeImageViewRound();
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

    public RegisterController()  {
    }

    public void makeImageViewRound() {
        double radius = profilePictureContainer.getPrefWidth() / 2;
        Circle clip = new Circle(radius, radius, radius);
        userProfilePicture.setClip(clip);
    }


    @FXML
    void moveToLoginView(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.setController(this.userApiClient);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean checkTextFields(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Empty fields", "Please fill in all fields");
            return false;
        } else if (username.length() < 6) {
            showAlert("Invalid Name", "Name should contain 6 or more characters.");
            return false;
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return false;
        } else if (password.length() < 6) {
            showAlert("Invalid password", "Password should contain 6 or more characters.");
        }

        return true;
    }

    public boolean checkPassword(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            showAlert("Error", "passwords don't match");
            return false;
        }
        return true;
    }


    @FXML
    void addProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter
                        ("image Files",
                        "*.png", "*.jpg", ".jpeg")
        );
        File selectedPicture = fileChooser.showOpenDialog(addPictureButton.getScene().getWindow());
        if (selectedPicture != null) {
            Image image = new Image(selectedPicture.toURI().toString());
            userProfilePicture.setImage(image);
            userProfilePicture.setFitWidth(profilePictureContainer.getPrefWidth());
            userProfilePicture.setFitHeight(profilePictureContainer.getPrefHeight());
            userProfilePicture.setPreserveRatio(false);
        }
    }

    @FXML
    void registerUser() {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatPasswordField.getText();
        if (!checkTextFields(username, email, password)) {
            return;
        }
        if (!checkPassword(password, repeatedPassword)) {
            return;
        }
        try {
            User user = new User(username, email, password);
            User checkIfUserExist= userApiClient.registerUser(user);
            if (checkIfUserExist==null) {
                showAlert("Existing User", "User already exists");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

package controller;

import javafx.scene.control.*;
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

import java.io.IOException;
import java.net.MalformedURLException;

public class RegisterController {

    UserApiClient userApiClient = new UserApiClient();
    User user;

    @FXML
    private Button addPictureButton;

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

    public RegisterController() throws MalformedURLException {
    }

    @FXML
    void addProfilePicture(ActionEvent event) {

    }

    @FXML
    void moveToLoginView(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
        Parent root = fxmlLoader.load();
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
    void registerUser(ActionEvent event) {
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
        User user = new User(username, email, password);
        userApiClient.registerUser(user);

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

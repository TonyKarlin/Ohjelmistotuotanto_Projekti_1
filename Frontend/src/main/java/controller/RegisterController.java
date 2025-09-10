package controller;

import javafx.scene.control.PasswordField;
import model.User;
import service.UserApiClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    public boolean checkPassword(String password, String repeatedPassword) {
        if (!password.equals( repeatedPassword)) {
            passwordField.clear();
            repeatPasswordField.clear();
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
        if (!checkPassword(password, repeatedPassword)) {
            return;
        }
        User user = new User(username, email, password);
        userApiClient.registerUser(user);

    }

}

package controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import model.UserResponse;
import request.LoginRequest;
import service.UserApiClient;
import utils.LanguageManager;
import utils.UIAlert;

public class LoginController {

    private UserApiClient userApiClient;

    private final UIAlert alert = new UIAlert();
    private UserResponse loginResponse = new UserResponse();

    public void setController(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;
    }

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label signUpLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    public void moveToRegisterView(MouseEvent event) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("localization.LanguageBundle", Locale.US);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/registerView.fxml"), bundle);
        Parent root = fxmlLoader.load();
        RegisterController controller = fxmlLoader.getController();
        controller.setController(this.userApiClient);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void login() throws IOException, InterruptedException {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            alert.showErrorAlert(LanguageManager.getString("login_empty_title"), LanguageManager.getString("login_empty"));
            return;
        }
        //Gets username and password from the text fields and calls the loginRequest method
        LoginRequest loginRequest = new LoginRequest(username, password);
        // Send the login request and save the result to a LoginResponse object
        loginResponse = userApiClient.loginUser(loginRequest);
        // If the login response is not null, extract the User and pass it to the next view
        if (loginResponse != null) {
            User user = loginResponse.getUser();
            user.setToken(loginResponse.getToken());
            moveToMainView(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            //If user is null show this error alert
        } else {
            alert.showErrorAlert(LanguageManager.getString("login_credentials_title"), LanguageManager.getString("login_credentials"));
        }
    }

    public void moveToMainView(User user) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chatDashboardView.fxml"));
        Parent root = fxmlLoader.load();
        ChatDashboardController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getString("title"));

        controller.setController(user, this.userApiClient);
        stage.setScene(new Scene(root));
        stage.show();
    }
}

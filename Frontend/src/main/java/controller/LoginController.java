package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import request.LoginRequest;
import model.User;
import service.UserApiClient;

import java.io.IOException;
import java.net.MalformedURLException;


public class LoginController {

    UserApiClient userApiClient;

    public LoginController()  {

    }

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registerView.fxml"));
        Parent root = fxmlLoader.load();
        RegisterController controller = fxmlLoader.getController();
        controller.setController(this.userApiClient);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void login() {
        try {
            String username = userNameTextField.getText();
            String password = passwordTextField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Empty fields", "Username or password field is empty");
                return;
            }
            LoginRequest loginRequest = new LoginRequest(username, password);
            User user = userApiClient.loginUser(loginRequest);
            moveToMainView(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Check credential", "Username or password is wrong");
            throw new RuntimeException(e);
        }
    }

    public void moveToMainView(User user) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainView.fxml"));
        Parent root = fxmlLoader.load();
        ChatDashboardController controller = fxmlLoader.getController();
        controller.setController(user, this.userApiClient);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
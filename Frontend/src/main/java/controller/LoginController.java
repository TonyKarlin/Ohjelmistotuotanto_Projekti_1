package controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import callback.LanguageChangeCallback;
import controller.component.LanguageButtonController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
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

public class LoginController implements LanguageChangeCallback {

    private UserApiClient userApiClient;

    private final UIAlert alert = new UIAlert();
    String languageBundle = "localization.LanguageBundle";
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

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
    private LanguageButtonController languageButtonController;

    /**
     * Called after FXML injection. Set up the language change callback.
     */
    @FXML
    public void initialize() {
        if (languageButtonController != null) {
            languageButtonController.setLanguageChangeCallback(this);
        }
    }

    /**
     * Handles language change by reloading the current view
     */
    @Override
    public void onLanguageChanged(Locale newLocale) {
        try {
            // Save the current input values
            String currentUsername = userNameTextField.getText();
            String currentPassword = passwordTextField.getText();

            // Reload the view with the new language
            ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, newLocale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"), bundle);
            Parent root = fxmlLoader.load();

            // Apply RTL orientation if needed
            if (LanguageManager.isRTL()) {
                root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }

            // Get the new controller and restore state
            LoginController newController = fxmlLoader.getController();
            newController.setController(this.userApiClient);
            newController.userNameTextField.setText(currentUsername);
            newController.passwordTextField.setText(currentPassword);

            // Replace the scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(LanguageManager.getString("title"));
        } catch (IOException e) {
            logger.info("Failed to reload view with new language: " + e.getMessage());
        }
    }

    @FXML
    public void moveToRegisterView(MouseEvent event) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/registerView.fxml"), bundle);
        Parent root = fxmlLoader.load();

        // Apply RTL orientation if needed
        if (LanguageManager.isRTL()) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

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
        LoginRequest loginRequest = new LoginRequest(username, password, null);
        // Send the login request and save the result to a LoginResponse object
        UserResponse loginResponse = userApiClient.loginUser(loginRequest);
        // If the login response is not null, extract the User and pass it to the next view
        if (loginResponse != null) {
            User user = loginResponse.getUser();
            user.setToken(loginResponse.getToken());

            String userLang = user.getLanguage();
            if (userLang != null && !userLang.isEmpty()) {
                Locale newLocale = Locale.forLanguageTag(userLang);
                LanguageManager.setLocale(newLocale);
            } else {
                LanguageManager.setLocale(Locale.ENGLISH);
            }

            moveToMainView(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            //If user is null show this error alert
        } else {
            alert.showErrorAlert(LanguageManager.getString("login_credentials_title"), LanguageManager.getString("login_credentials"));
        }
    }

    public void moveToMainView(User user) throws IOException, InterruptedException {
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"), bundle);
        Parent root = fxmlLoader.load();

        // Apply RTL orientation if needed
        if (LanguageManager.isRTL()) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }

        MainViewController controller = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getString("title"));

        controller.setController(user, this.userApiClient);
        stage.setScene(new Scene(root));
        stage.show();
    }
}

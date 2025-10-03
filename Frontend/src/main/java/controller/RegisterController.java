package controller;


import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.UserApiClient;
import utils.UIAlert;

public class RegisterController {

    private UserApiClient userApiClient;
    private UIAlert alert = new UIAlert();

    public RegisterController() {}

    //Controller to set instances. Is called when changing to this view.
    public void setController(UserApiClient userApiClient) {
        this.userApiClient = userApiClient;
    }

    //region FXML-injected UI components
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
    //endregion


    //To move back to login view
    @FXML
    void moveToLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        // pass the userApiClient instance back to login view
        controller.setController(this.userApiClient);
        Stage stage = (Stage) loginLabel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    //Register user to db
    @FXML
    void registerUser() {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatPasswordField.getText();
//        if (!checkTextFields(username, email, password)) {
//            return;
//        }
        if (!checkPassword(password, repeatedPassword)) {
            return;
        }
        try {
            //creates user object from the user inputs
            User user = new User(username, email, password);
            //Sends the user object to the server and creates another user from the backend response
            User checkIfUserExist = userApiClient.registerUser(user);
            //If response is not user information but response message, user is null so send this alert message
            if (checkIfUserExist == null) {
                alert.showErrorAlert("Existing User", "User already exists");
            } else {
                // Registration successful, move to login view
                try {
                    alert.showSuccessAlert("Success", "User created successfully ✅");
                    moveToLoginView();
                } catch (IOException e) {
                    alert.showErrorAlert("Error", "Could not load login view.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}

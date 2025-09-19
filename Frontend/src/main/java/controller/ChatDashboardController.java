package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.User;
import service.UserApiClient;

import java.io.IOException;

public class ChatDashboardController {

    User loggedInUser;
    UserApiClient userApiClient;

    public void setController(User loggedInUser, UserApiClient userApiClient) {
        this.loggedInUser = loggedInUser;
        this.userApiClient = userApiClient;
        setUpUsername();

    }

    @FXML
    private Button addFriendsButton;

    @FXML
    private ImageView contactUserPicture;

    @FXML
    private Circle contactUserStatus;

    @FXML
    private Label contactUsername;

    @FXML
    private Button createGroupButton;

    @FXML
    private Label loggedInUsername;

    @FXML
    private Label messageLabel;

    @FXML
    private Button messageOptionsButton;

    @FXML
    private Label messageTimeLabel;

    @FXML
    private Button sendMessageButton;

    @FXML
    private TextField sendMessageTextField;

    @FXML
    private ImageView userProfilePicture;

    public void setUpUsername() {
        loggedInUsername.setText(loggedInUser.getUsername());
    }

    @FXML
    public void openAddFriendsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/popup/addFriendsView.fxml"));
        Parent root = fxmlLoader.load();
        AddFriendsController controller = fxmlLoader.getController();
        controller.setController(loggedInUser, this.userApiClient);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

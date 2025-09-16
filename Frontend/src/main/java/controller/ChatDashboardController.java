package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.User;
import service.UserApiClient;

public class ChatDashboardController {

    private User loggedInuser;
    private UserApiClient userApiClient;

    public ChatDashboardController() {

    }

    public void setController(User user, UserApiClient userApiClient) {
        this.loggedInuser = user;
        this.userApiClient = userApiClient;
        setUsernameLabel();
    }

    @FXML
    private Button addFriendsButton;

    @FXML
    private ImageView contactProfilePicture;

    @FXML
    private Label contactUsername;

    @FXML
    private Button createGroupButton;

    @FXML
    private MenuItem fileMenuItem;

    @FXML
    private MenuItem imageMenuItem;

    @FXML
    private MenuItem messageDeleteButton;

    @FXML
    private MenuItem messageModifyButton;

    @FXML
    private MenuItem messageReactionButton;

    @FXML
    private Label messageTimeLabel;

    @FXML
    private Button messagemenuButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField sendMessageTextField;

    @FXML
    private ImageView userProfilePicture;

    @FXML
    private Label userUsername;


    public void setUsernameLabel() {
        userUsername.setText(loggedInuser.getUsername());
    }

    @FXML
    void deleteMessage(ActionEvent event) {

    }

    @FXML
    void modifyMessage(ActionEvent event) {

    }

    @FXML
    void openContactProfilePage(MouseEvent event) {

    }

    @FXML
    void openUserProfilePage(MouseEvent event) {

    }

    @FXML
    void reactMessage(ActionEvent event) {

    }

}


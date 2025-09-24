package controller;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import model.User;


public class UserProfileController {

    User loggedInUser;

    public void setController(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        addUserInformation(loggedInUser);
    }

    @FXML
    private Circle awayStatus;

    @FXML
    private Circle userCurrentStatus;

    @FXML
    private Button changeInformationButton;

    @FXML
    private Button changeUserPictureButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private Circle offlineStatus;

    @FXML
    private Circle onlineStatus;

    @FXML
    private PasswordField passwordField;

    @FXML
    private StackPane profilePictureContainer;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private ImageView userProfilePicture;

    @FXML
    private TextField usernameTextField;

    @FXML
    public void changeInformation(ActionEvent event) {

    }

    @FXML
    public void changeUserStatus(MouseEvent  event) {
        Circle clickedCircle = (Circle) event.getSource();
        userCurrentStatus.setFill(clickedCircle.getFill());

    }

        public void addUserInformation (User loggedInUser){
            emailTextField.setText(loggedInUser.getEmail());
            usernameTextField.setText(loggedInUser.getUsername());
        }

    }


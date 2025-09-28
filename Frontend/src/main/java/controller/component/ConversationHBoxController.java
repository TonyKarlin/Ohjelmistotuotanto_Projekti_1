package controller.component;

import controller.ChatDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import lombok.Data;
import model.Conversation;
import utils.ImageRounder;

import java.io.IOException;


@Data
public class ConversationHBoxController {

    Conversation conversation;
    ChatDashboardController parentController;
    ImageRounder imageRounder;

    public void setController(Conversation conversation, ChatDashboardController parentController) {
        this.conversation = conversation;
        this.parentController = parentController;
        imageRounder = new ImageRounder(contactUserPicture);
    }


    //region FXML-injected UI components
    @FXML
    private MenuItem addUserButton;
    @FXML
    private StackPane contactContent;
    @FXML
    private ImageView contactUserPicture;
    @FXML
    private Circle contactUserStatus;
    @FXML
    private Label contactUsername;
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem leaveButton;
    @FXML
    private MenuItem removeUserButton;
    @FXML
    private TextField userIdTextField;
    @FXML
    private MenuButton conversationOptionButton;
    //endregion


    public void setUsername(String username) {
        contactUsername.setText(username);
    }

    public void setUserImage(Image image) {
        contactUserPicture.setImage(image);
    }


    @FXML
    public void openMessages() throws IOException, InterruptedException {
        parentController.showConversationMessages(conversation, conversationOptionButton);
    }

    //Todo: options for conversation
    @FXML
    public void adduserToConversation(ActionEvent event) {
    }

    @FXML
    public void deleteConversation(ActionEvent event) {
    }

    @FXML
    public void leaveFromConversation(ActionEvent event) {
    }

    @FXML
    public void removeUserFromConversation(ActionEvent event) {
    }

}

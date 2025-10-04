package controller.component;

import controller.ChatDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import utils.ImageRounder;

import java.io.IOException;
import java.util.Objects;

public class ConversationHBoxController {

    Conversation conversation;
    ChatDashboardController parentController;
    ImageRounder imageRounder;
    User loggedInuser;

    public void setController(Conversation conversation, ChatDashboardController parentController, User loggedInUser) {
        this.conversation = conversation;
        this.parentController = parentController;
        this.loggedInuser = loggedInUser;
        imageRounder = new ImageRounder(conversationProfilePicture);
        setConversationInformation(conversation);
    }


    //region FXML-injected UI components
    @FXML
    private ImageView conversationProfilePicture;
    @FXML
    private Circle contactUserStatus;
    @FXML
    private Label conversationName;
    @FXML
    private Button conversationsettingsButton;
    //endregion


    public void setConversationInformation(Conversation conversation) {
        if (Objects.equals(conversation.getType(), "PRIVATE")) {
            for (ConversationParticipant p : conversation.getParticipants()) {
                if (p.getUserId() != loggedInuser.getId()) {
                    conversationName.setText(p.getUsername());
                    break;
                }
            }
        } else conversationName.setText(conversation.getName());
    }

    public void setUserImage(Image image) {
        conversationProfilePicture.setImage(image);
    }

    @FXML
    public void openConversationSettings() throws IOException {
        parentController.openConversationSettings(this.conversation, this);
    }

    @FXML
    public void openMessages() throws IOException, InterruptedException {
        parentController.showConversationMessages(conversation, conversationsettingsButton);
    }

}

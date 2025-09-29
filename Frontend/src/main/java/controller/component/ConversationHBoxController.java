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
import utils.ImageRounder;
import java.io.IOException;

public class ConversationHBoxController {

    Conversation conversation;
    ChatDashboardController parentController;
    ImageRounder imageRounder;

    public void setController(Conversation conversation, ChatDashboardController parentController) {
        this.conversation = conversation;
        this.parentController = parentController;
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
        System.out.println(conversation.getName());
        System.out.println(conversation.getId());
        conversationName.setText(conversation.getName());
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

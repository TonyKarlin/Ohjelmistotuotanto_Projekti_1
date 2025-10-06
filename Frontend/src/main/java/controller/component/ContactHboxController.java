package controller.component;

import java.io.IOException;

import controller.ChatDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import lombok.Data;
import model.Contact;
import model.Conversation;
import model.User;
import service.ConversationApiClient;
import utils.ImageRounder;
import utils.UIAlert;

@Data
public class ContactHboxController {

    Contact contact;
    ChatDashboardController parentController;
    ImageRounder imageRounder;
    Conversation conversation;
    User loggedInuser;
    boolean showAddButton = false;
    ConversationApiClient conversationApiClient;
    UIAlert alert = new UIAlert();;

    public void setController(Contact contact, ChatDashboardController parentController) {
        this.contact = contact;
        this.parentController = parentController;
        imageRounder = new ImageRounder(contactUserPicture);
    }

    //This controller is called when adding friends in the ConversationSettingsController to the ScrollPane
    public void setController(Conversation conversation, User loggedInUser) {
        this.conversation = conversation;
        this.loggedInuser = loggedInUser;
        this.showAddButton = true;
        addButton.setVisible(true);
        conversationApiClient = new ConversationApiClient();

    }

    @FXML
    private ImageView contactUserPicture;

    @FXML
    private Circle contactUserStatus;

    @FXML
    private Label contactUsername;

    @FXML
    private Button addButton;

    public void setUsername(String username) {
        contactUsername.setText(username);
    }

    public void setUserImage(Image image) {
        contactUserPicture.setImage(image);
    }

    @FXML
    public void openContactProfile() throws IOException, InterruptedException {
        // TODO: to be implemented, open the users profile
        System.out.println("To be implemented");
    }

    @FXML
    public void addToConversation()  {
        try {
            System.out.println(contact.getId());
            System.out.println(contact.getContactUserId());
            boolean success = conversationApiClient.addUserToConversation(conversation.getId(), contact.getContactUserId(), loggedInuser.getToken());
            if (success) {
                alert.showSuccessAlert("Success", "User added to the conversation: " + conversation.getName());
            } else {
                alert.showErrorAlert("Failed", "failed to add user to the conversation");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

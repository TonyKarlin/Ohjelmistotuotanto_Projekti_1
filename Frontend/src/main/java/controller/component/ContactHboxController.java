package controller.component;

import java.io.IOException;

import controller.ConversationSettingsController;
import controller.CreateGroupController;
import controller.MainViewController;
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

@Data
public class ContactHboxController {

    Contact contact;
    MainViewController parentController;
    ImageRounder imageRounder;
    Conversation conversation;
    User loggedInuser;
    boolean showAddButton = false;
    private boolean selected = false;
    ConversationApiClient conversationApiClient;
    CreateGroupController createGroupController;
    ConversationSettingsController conversationSettingsController;

    public void setController(Contact contact, MainViewController parentController) {
        this.contact = contact;
        this.parentController = parentController;
        imageRounder = new ImageRounder(contactUserPicture);
    }

    //This controller is called when adding friends in the ConversationSettingsController to the ScrollPane
    public void setControllerForConversationSettings(Conversation conversation, User loggedInUser, ConversationSettingsController conversationSettingsController) {
        this.conversation = conversation;
        this.loggedInuser = loggedInUser;
        this.showAddButton = true;
        this.conversationSettingsController = conversationSettingsController;
        addButton.setVisible(true);
        conversationApiClient = new ConversationApiClient();

    }

    // For create group screen
    public void setControllerForCreateGroup(CreateGroupController createGroupController, Contact contact) {
        this.createGroupController = createGroupController;
        this.contact = contact;
        addButton.setVisible(true);
        contactUsername.setText(contact.getContactUsername());
    }

    //region FXML-injected UI components
    @FXML
    private ImageView contactUserPicture;
    @FXML
    private Circle contactUserStatus;
    @FXML
    private Label contactUsername;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    //endregion

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

    public void addToConversation() throws IOException, InterruptedException {
        if (conversationSettingsController != null) {
            conversationSettingsController.addUserToConversation(contact, this);
        } else if (createGroupController != null) {
            createGroupController.handleSelectContact(contact, this);
        }
    }

}

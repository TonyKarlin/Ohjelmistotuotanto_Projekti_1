package controller;

import java.io.IOException;
import java.util.List;

import callback.ContactUpdateCallback;
import controller.component.ContactHboxController;
import controller.component.ConversationHBoxController;
import controller.component.MessageHBoxController;
import controller.component.SendMessageHBoxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Data;
import model.Contact;
import model.Conversation;
import model.Message;
import model.User;
import service.ContactApiClient;
import service.ConversationApiClient;
import service.MessageApiClient;
import service.UserApiClient;
import utils.ImageRounder;

@Data
public class ChatDashboardController implements ContactUpdateCallback {

    User loggedInUser;
    UserApiClient userApiClient;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    MessageApiClient messageApiClient = new MessageApiClient();
    ContactApiClient contactApiClient = new ContactApiClient();
    List<Conversation> conversations;
    List<Contact> contacts;
    List<Contact> pendingContacts;
    List<Contact> sentContacts;
    private ImageRounder imageRounder;
    private MenuButton activeConversationMenu;

    public void setController(User loggedInUser, UserApiClient userApiClient) throws IOException, InterruptedException {
        this.loggedInUser = loggedInUser;
        this.userApiClient = userApiClient;
        setUpUsername();
        this.conversations = getUserConversations();
        this.contacts = getUserContacts();
        imageRounder = new ImageRounder(userProfilePicture);
        setUpUsername();
        addConversation();
        addFriendsToFriendsList();
    }

    //region FXML-injected UI components
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private StackPane userProfileContent;
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
    private BorderPane contentBorderPane;
    @FXML
    private VBox VBoxContentPane;
    @FXML
    private VBox contactVBox;
    @FXML
    private VBox friendsList;
    //endregion

    public void setUpUsername() {
        loggedInUsername.setText(loggedInUser.getUsername());
    }

    public List<Conversation> getUserConversations() throws IOException, InterruptedException {
        return conversationApiClient.getAllUserConversations(loggedInUser);
    }

    public List<Contact> getUserContacts() throws IOException, InterruptedException {
        return contactApiClient.getAllUserContacts(loggedInUser);
    }

    //ImageView userProfilePicture opens up a user profile view and clears any content in the Content pane
    @FXML
    public void openUserProfile(MouseEvent event) throws IOException {
        //Clears send message HBox in the bottom and main view
        contentBorderPane.setBottom(null);
        VBoxContentPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/userProfileView.fxml"));
        VBox userProfile = fxmlLoader.load();
        UserProfileController controller = fxmlLoader.getController();
        //Pass user, userApiClient and this view controller instances to user profile view
        controller.setController(loggedInUser, this.userApiClient, this);
        // Adds the user profile to the VBox element
        VBoxContentPane.getChildren().add(userProfile);
    }

    //Opens the add friends view
    @FXML
    public void openAddFriendsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addFriendsView.fxml"));
        Parent root = fxmlLoader.load();
        AddFriendsController controller = fxmlLoader.getController();
        //Pass instances to the view
        controller.setController(loggedInUser, this.userApiClient, this.contactApiClient, this.contacts, this);
        Stage stage = new Stage();
        //Modality blocks all windows of this application
        stage.initModality(Modality.APPLICATION_MODAL);
        //Undecorated so no resize, close or fullscreen options
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
    }

    //Logout user and clears the instances
    @FXML
    public void logoutUser(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        loggedInUser = null;
        conversations = null;
        contacts = null;
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        LoginController controller = fxmlLoader.getController();
        controller.setController(this.userApiClient);
        stage.show();
    }


    public void showConversationMessages(Conversation conversation, MenuButton menuButton) throws IOException, InterruptedException {
        VBoxContentPane.getChildren().clear();
        if (activeConversationMenu != null) {
            activeConversationMenu.setVisible(false);
        }
        activeConversationMenu = menuButton;
        activeConversationMenu.setVisible(true);
        List<Message> messages = messageApiClient.getConversationMessages(conversation, this.loggedInUser);
        if (messages != null && !messages.isEmpty()) {
            for (Message m : messages) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/messageHBox.fxml"));
                HBox messageHBox = fxmlLoader.load();
                MessageHBoxController controller = fxmlLoader.getController();
                controller.setController(m, this);
                controller.setConversationId(conversation.getId());
                controller.setId(m.getId());
                controller.setSenderId(m.getSenderId());
                controller.setMessageInformation(m.getText(), m.getCreatedAt(), m.getSenderUsername());
                VBoxContentPane.getChildren().add(messageHBox);
            }
        }
        sendMessageComponent(conversation);

    }

    public void sendMessageComponent(Conversation conversation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/sendMessageHBox.fxml"));
        HBox sendMessageHBox = fxmlLoader.load();
        SendMessageHBoxController controller = fxmlLoader.getController();
        controller.setController(conversation, this.loggedInUser);
        contentBorderPane.setBottom(sendMessageHBox);
    }

    //Adds the conversation components
    public void addConversation() throws IOException {
        for (Conversation c : conversations) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/conversationHBox.fxml"));
            HBox userConversationHBox = loader.load();
            ConversationHBoxController controller = loader.getController();
            controller.setController(c, this);
            controller.setUsername(c.getName());
            contactVBox.getChildren().add(userConversationHBox);
        }
    }

    public void addFriendsToFriendsList() throws IOException {
        for (Contact contact : contacts) {
            if ("ACCEPTED".equals(contact.getStatus())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/contactHBox.fxml"));
                HBox userContactsHbox = loader.load();
                ContactHboxController controller = loader.getController();
                controller.setController(contact, this);
                controller.setUsername(contact.getContactUsername());
                friendsList.getChildren().add(userContactsHbox);
            }
        }
    }

    @Override
    public void onContactsUpdated(List<Contact> updatedContacts) throws IOException {
        // Update the contactslist when change has happened
        this.contacts = updatedContacts;

        // Clear the current friends list UI
        friendsList.getChildren().clear();

        // Refresh the friends list UI with updated contacts
        addFriendsToFriendsList();
    }
}

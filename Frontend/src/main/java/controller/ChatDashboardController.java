package controller;

import java.io.IOException;
import java.util.List;

import controller.component.ConversationHBoxController;
import controller.component.MessageHBoxController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Contact;
import model.Conversation;
import model.Message;
import model.User;
import service.ContactApiClient;
import service.ConversationApiClient;
import service.MessageApiClient;
import service.UserApiClient;

public class ChatDashboardController {

    User loggedInUser;
    UserApiClient userApiClient;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    List<Conversation> conversations;

    MessageApiClient messageApiClient = new MessageApiClient();

    ContactApiClient contactApiClient = new ContactApiClient();
    List<Contact> contacts;

    public void setController(User loggedInUser, UserApiClient userApiClient) throws IOException, InterruptedException {
        this.loggedInUser = loggedInUser;
        this.userApiClient = userApiClient;
        setUpUsername();
        conversations = getUserConversations();
        // contacts = getUserContacts();
        // // addConversation();
        // addFriendsToFriendsList();

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

    @FXML
    private BorderPane contentBorderPane;

    @FXML
    private VBox VBoxContentPane;

    @FXML
    private VBox contactVBox;

    @FXML
    private VBox friendsList;

    public void setUpUsername() {
        loggedInUsername.setText(loggedInUser.getUsername());
    }

    public List<Conversation> getUserConversations() throws IOException, InterruptedException {
        return conversationApiClient.getConversationsById(loggedInUser);
    }

    public List<Contact> getUserContacts() throws IOException, InterruptedException {
        return contactApiClient.getAllUserContacts(loggedInUser);
    }

    @FXML
    public void openUserProfile(MouseEvent event) throws IOException {
        VBoxContentPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/userProfileView.fxml"));
        VBox userProfile = fxmlLoader.load();
        UserProfileController controller = fxmlLoader.getController();
        controller.setController(loggedInUser);
        VBoxContentPane.getChildren().add(userProfile);

    }

    public void showConversationMessages(Conversation conversation) throws IOException, InterruptedException {
        VBoxContentPane.getChildren().clear();
        List<Message> messages = messageApiClient.getConversationMessages(conversation);
        for (Message m : messages) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/messageHBox.fxml"));
            HBox messageHBox = fxmlLoader.load();
            MessageHBoxController controller = fxmlLoader.getController();
            controller.setController(m);
            controller.setId(m.getId());
            controller.setMessageInformation(m.getText(), m.getCreatedAt(), m.getSenderUsername());

            VBoxContentPane.getChildren().add(messageHBox);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/sendMessageHBox.fxml"));
        HBox sendMessageHBox = fxmlLoader.load();
        contentBorderPane.setBottom(sendMessageHBox);
    }

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
        for (Contact c : contacts) {
            friendsList.getChildren().add(new Label(c.getContactUsername()));
        }
    }

    @FXML
    public void openAddFriendsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addFriendsView.fxml"));
        Parent root = fxmlLoader.load();
        AddFriendsController controller = fxmlLoader.getController();
        controller.setController(loggedInUser, this.userApiClient);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.show();
    }

}

package controller;

import controller.component.MessageHBoxController;
import controller.component.ConversationHBoxController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Conversation;
import model.Message;
import model.User;
import service.ConversationApiClient;
import service.MessageApiClient;
import service.UserApiClient;

import java.io.IOException;
import java.util.List;

public class ChatDashboardController {

    User loggedInUser;
    UserApiClient userApiClient;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    List<Conversation> conversations;
    MessageApiClient messageApiClient = new MessageApiClient();

    public void setController(User loggedInUser, UserApiClient userApiClient) throws IOException, InterruptedException {
        this.loggedInUser = loggedInUser;
        this.userApiClient = userApiClient;
        setUpUsername();
        conversations = getUserConversations();
        addConversation();

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
    private VBox messageVBox;

    @FXML
    private VBox contactVBox;

    public void setUpUsername() {
        loggedInUsername.setText(loggedInUser.getUsername());
    }

    public List<Conversation> getUserConversations() throws IOException, InterruptedException {
        return conversationApiClient.getConversationsById(loggedInUser);
    }

    public void showConversationMessages(Conversation conversation) throws IOException, InterruptedException {
        messageVBox.getChildren().clear();
        List<Message>  messages = messageApiClient.getConversationMessages(conversation);
        for (Message m : messages) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/messageHBox.fxml"));
            HBox messageHBox = fxmlLoader.load();
            MessageHBoxController controller = fxmlLoader.getController();
            controller.setController(m);
            controller.setId(m.getId());
            controller.setText(m.getText());
            controller.setTime(m.getCreatedAt());
            messageVBox.getChildren().add(messageHBox);

        }
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

    @FXML
    public void openAddFriendsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addFriendsView.fxml"));
        Parent root = fxmlLoader.load();
        AddFriendsController controller = fxmlLoader.getController();
        controller.setController(loggedInUser, this.userApiClient);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

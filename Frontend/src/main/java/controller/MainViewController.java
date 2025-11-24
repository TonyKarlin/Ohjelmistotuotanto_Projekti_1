package controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import callback.ContactUpdateCallback;
import callback.LanguageChangeCallback;
import controller.component.ContactHboxController;
import controller.component.ConversationHBoxController;
import controller.component.LanguageButtonController;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import model.Contact;
import model.Conversation;
import model.Message;
import model.User;
import service.ContactApiClient;
import service.ConversationApiClient;
import service.MessageApiClient;
import service.UserApiClient;
import utils.ImageRounder;
import utils.LanguageManager;

public class MainViewController implements ContactUpdateCallback, LanguageChangeCallback {

    User loggedInUser;
    UserApiClient userApiClient;
    ConversationApiClient conversationApiClient = new ConversationApiClient();
    MessageApiClient messageApiClient = new MessageApiClient();
    ContactApiClient contactApiClient = new ContactApiClient();
    List<Conversation> conversations;
    List<Contact> contacts;
    List<Contact> pendingContacts;
    List<Contact> sentContacts;
    ImageRounder imageRounder;
    Button activeConversation;
    String languageBundle = "localization.LanguageBundle";
    String controllerString = "controller";
    private static final String CONTACT_HBOX_FXML = "/component/contactHBox.fxml";
    private static final Logger logger = Logger.getLogger(MainViewController.class.getName());

    // Sets the controller with user and API clients, initializes user info and lists
    public void setController(User loggedInUser, UserApiClient userApiClient) throws IOException, InterruptedException {
        this.loggedInUser = loggedInUser;
        this.userApiClient = userApiClient;
        setUserInformation();
        this.conversations = getUserConversations();
        this.contacts = getUserContacts();
        addConversations("PRIVATE");
        addFriendsToFriendsList();
    }

    //region FXML-injected UI components
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private StackPane profilePictureContainer;
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
    private Button conversationSettingsButton;
    @FXML
    private TextField sendMessageTextField;
    @FXML
    private BorderPane contentBorderPane;
    @FXML
    private VBox vBoxContentPane;
    @FXML
    private VBox conversationVBox;
    @FXML
    private VBox friendsList;
    @FXML
    private Button pendingButton;
    @FXML
    private Button friendButton;

    @FXML
    private LanguageButtonController languageButtonController;
    //endregion

    /**
     * Called after FXML injection. Set up the language change callback.
     */
    @FXML
    public void initialize() {
        if (languageButtonController != null) {
            languageButtonController.setLanguageChangeCallback(this);
        }
    }

    /**
     * Handles language change by reloading the current view with user data
     * preserved
     */
    @Override
    public void onLanguageChanged(Locale newLocale) throws RuntimeException {
        try {
            // Reload the view with the new language
            ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, newLocale);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"), bundle);
            Parent root = fxmlLoader.load();

            // Apply RTL orientation if needed
            if (LanguageManager.isRTL()) {
                root.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            }

            // Get the new controller and restore state
            MainViewController newController = fxmlLoader.getController();
            newController.setController(this.loggedInUser, this.userApiClient);

            // Replace the scene
            Stage stage = (Stage) loggedInUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(LanguageManager.getString("title"));
        } catch (IOException e) {
            logger.info("Failed to reload view with new language: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted while changing language", e);
        }
    }

    // Call this method in the child component when the user object needs to be updated
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // Returns the currently logged-in user
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public List<Conversation> getConversations() {
        return this.conversations;
    }

    // Sets user information in the UI
    public void setUserInformation() {
        loggedInUsername.setText(loggedInUser.getUsername());
        Image profilePicture = new Image(loggedInUser.getProfilePictureUrl());
        userProfilePicture.setImage(profilePicture);
        imageRounder = new ImageRounder(userProfilePicture);
        userProfilePicture.setImage(profilePicture);
        userProfilePicture.setPreserveRatio(false);
    }

    // Retrieves all conversations for the user
    public List<Conversation> getUserConversations() throws IOException, InterruptedException {
        return conversationApiClient.getAllUserConversations(loggedInUser);
    }

    // Retrieves all contacts for the user
    public List<Contact> getUserContacts() throws IOException, InterruptedException {
        return contactApiClient.getAllUserContacts(loggedInUser);
    }

    // Opens the user profile view and clears main content
    @FXML
    public void openUserProfile(MouseEvent event) throws IOException {
        //Clears send message HBox in the bottom and main view
        contentBorderPane.setBottom(null);
        vBoxContentPane.getChildren().clear();
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/userProfileView.fxml"), bundle);
        VBox userProfile = fxmlLoader.load();
        UserProfileController controller = fxmlLoader.getController();
        //Pass user, userApiClient and this view controller instances to user profile view
        controller.setController(loggedInUser, this.userApiClient, this);
        // Adds the user profile to the VBox element
        vBoxContentPane.getChildren().add(userProfile);
    }

    // Opens the add friends view in a modal window
    @FXML
    public void openAddFriendsView() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addFriendsView.fxml"), bundle);
        Parent root = fxmlLoader.load();
        AddFriendsController controller = fxmlLoader.getController();
        //Pass instances to the view
        controller.setController(loggedInUser, this.userApiClient, this.contacts, this);
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
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"), bundle);
        Parent root = fxmlLoader.load();

        // Apply RTL orientation if needed
        if (LanguageManager.isRTL()) {
            root.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
        }

        Stage stage = new Stage();
        stage.setTitle(LanguageManager.getString("title"));
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

    @FXML
    public void openGroupConversations() throws IOException {
        addConversations("GROUP");
    }

    @FXML
    public void openPrivateConversations() throws IOException {
        addConversations("PRIVATE");
    }

    // Adds conversation components to the UI
    public void addConversations(String groupType) throws IOException {
        conversationVBox.getChildren().clear();
        for (Conversation c : conversations) {
            if (Objects.equals(c.getType(), groupType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/conversationHBox.fxml"));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                loader.setResources(bundle);
                HBox userConversationHBox = loader.load();
                ConversationHBoxController controller = loader.getController();
                controller.setController(c, this, loggedInUser);
                conversationVBox.getChildren().add(userConversationHBox);
            }
        }
    }

    public void openConversationSettings(Conversation conversation, ConversationHBoxController conversationHBoxController) throws IOException {
        contentBorderPane.setBottom(null);
        vBoxContentPane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/conversationSettingsView.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        fxmlLoader.setResources(bundle);
        VBox conversationSettings = fxmlLoader.load();
        ConversationSettingsController controller = fxmlLoader.getController();
        controller.setController(this.loggedInUser, conversation, this,
                conversationHBoxController, contacts);

        vBoxContentPane.getChildren().add(conversationSettings);
    }

    // Shows messages for a selected conversation
    public void showConversationMessages(Conversation conversation, Button conversationSettingsButton) throws IOException, InterruptedException {
        vBoxContentPane.getChildren().clear();
        //If conversation is private don't show the conversation settings button
        if (activeConversation != null) {
            activeConversation.setVisible(false);
        }
        if (Objects.equals(conversation.getType(), "GROUP")) {
            activeConversation = conversationSettingsButton;
            activeConversation.setVisible(true);
        } else {
            activeConversation = null;
            conversationSettingsButton.setVisible(false);
        }
        List<Message> messages = messageApiClient.getConversationMessages(conversation, this.loggedInUser);
        if (messages != null && !messages.isEmpty()) {
            for (Message m : messages) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/messageHBox.fxml"));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                fxmlLoader.setResources(bundle);
                HBox messageHBox = fxmlLoader.load();
                MessageHBoxController controller = fxmlLoader.getController();
                controller.setController(m, this, conversation);
                messageHBox.getProperties().put(controllerString, controller);
                vBoxContentPane.getChildren().add(messageHBox);
            }
        }
        sendMessageComponent(conversation);
    }

    //This method called in the SendMessageHBoXController to show the message locally
    public void addMessageToConversation(Message message, Conversation conversation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/messageHBox.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        fxmlLoader.setResources(bundle);
        HBox messageHBox = fxmlLoader.load();
        MessageHBoxController controller = fxmlLoader.getController();
        controller.setController(message, this, conversation);
        messageHBox.getProperties().put(controllerString, controller);
        vBoxContentPane.getChildren().add(messageHBox);
    }

    //This method called in the MessageHBoxController
    public void deleteMessageLocally(Message message) throws IOException {
        vBoxContentPane.getChildren().removeIf(node -> {
            if (node instanceof HBox) {
                Object controller = ((HBox) node).getProperties().get(controllerString);
                if (controller instanceof MessageHBoxController msgController) {
                    return msgController.getMessage().getId() == message.getId();
                }
            }
            return false;
        });
    }

    // Loads and displays the send message component
    public void sendMessageComponent(Conversation conversation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/component/sendMessageHBox.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        fxmlLoader.setResources(bundle);
        HBox sendMessageHBox = fxmlLoader.load();
        SendMessageHBoxController controller = fxmlLoader.getController();
        controller.setController(conversation, this.loggedInUser, this);
        contentBorderPane.setBottom(sendMessageHBox);
    }

    // Adds accepted friends to the friends list UI
    public void addFriendsToFriendsList() throws IOException {
        for (Contact contact : contacts) {
            if ("ACCEPTED".equals(contact.getStatus())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTACT_HBOX_FXML));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                loader.setResources(bundle);
                HBox userContactsHbox = loader.load();
                ContactHboxController controller = loader.getController();
                controller.setController(contact, this);
                controller.setUsername(contact.getContactUsername());
                friendsList.getChildren().add(userContactsHbox);
            }
        }
    }

    @FXML
    public void openCreateGroupView() throws IOException {
        contentBorderPane.setBottom(null);
        vBoxContentPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/createGroupConversationView.fxml"));
        ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
        loader.setResources(bundle);
        VBox createGroupVBox = loader.load();
        CreateGroupController controller = loader.getController();
        controller.setController(contacts, loggedInUser, this);
        controller.setBundle(bundle);
        vBoxContentPane.getChildren().add(createGroupVBox);
    }

    @FXML
    public void openFriendList() throws IOException {

        friendsList.getChildren().clear();

        for (Contact contact : contacts) {
            if ("ACCEPTED".equals(contact.getStatus())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTACT_HBOX_FXML));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                loader.setResources(bundle);
                HBox userContactsHbox = loader.load();
                ContactHboxController controller = loader.getController();
                controller.setController(contact, this);
                controller.setUsername(contact.getContactUsername());
                friendsList.getChildren().add(userContactsHbox);
            }
        }

    }

    @FXML
    public void openPendingList() throws IOException {

        friendsList.getChildren().clear();

        for (Contact contact : contacts) {
            if ("PENDING".equals(contact.getStatus())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTACT_HBOX_FXML));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                loader.setResources(bundle);
                HBox userContactsHbox = loader.load();
                ContactHboxController controller = loader.getController();
                controller.setController(contact, this);
                controller.setUsername(contact.getContactUsername());
                friendsList.getChildren().add(userContactsHbox);
            }
        }
    }

    @FXML
    public void openSentList() throws IOException {
        friendsList.getChildren().clear();

        for (Contact contact : contacts) {
            if ("SENT".equals(contact.getStatus())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(CONTACT_HBOX_FXML));
                ResourceBundle bundle = ResourceBundle.getBundle(languageBundle, LanguageManager.getCurrentLocale());
                loader.setResources(bundle);
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

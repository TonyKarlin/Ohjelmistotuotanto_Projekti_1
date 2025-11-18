package controller.component;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

public class ConversationParticipantHBoxController {

    ConversationParticipant participant;
    ConversationRequest conversationRequest;
    Conversation conversation;
    User loggedInuser;
    HBox hbox;
    ListView<HBox> listView;
    ConversationApiClient conversationApiClient = new ConversationApiClient();

    public void setController(ConversationParticipant participant, Conversation conversation, User loggedInuser, HBox hbox, ListView<HBox> listView) {
        this.participant = participant;
        this.conversation = conversation;
        this.loggedInuser = loggedInuser;
        this.hbox = hbox;
        this.listView = listView;
        setParticipantInformation();
    }

    @FXML
    private Button Remove;

    @FXML
    private Circle contactUserStatus;

    @FXML
    private ImageView participantProfilePicture;

    @FXML
    private Label participantUsername;

    public void setParticipantInformation() {
        participantUsername.setText(participant.getUsername());
    }

    @FXML
    public void removeParticipantFromConversation() throws IOException, InterruptedException {
        boolean success = conversationApiClient.removeUserFromConversation(participant.getUserId(), conversation.getId(), loggedInuser.getToken());
        if (success) {
            listView.getItems().remove(hbox);
            conversation.getParticipants().remove(participant);
        } else {
            System.out.println("Failed to remove participant");
        }
    }

}

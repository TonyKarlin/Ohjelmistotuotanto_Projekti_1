import model.Contact;
import model.Conversation;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ConversationRequest;
import request.ConversationUpdateRequest;
import service.ConversationApiClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConversationApiClientTest {

    private Conversation conversation;
    private ConversationRequest conversationRequest;
    private ConversationApiClient conversationApiClient;
    ConversationUpdateRequest conversationUpdateRequest;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        conversation = new Conversation();
        conversationApiClient = new ConversationApiClient();
        conversationUpdateRequest = new ConversationUpdateRequest();
        user = new User();
    }

    /**
     * Tests creating a new conversation with a creator and participants.
     */
    @Test
    public void testCreateConversation() throws IOException, InterruptedException {
        user.setId(1);
        List<Integer> participantIds = Arrays.asList(2, 3);
        String name = "toimiko";
        conversationRequest = new ConversationRequest(user.getId(), name, participantIds);
        conversation = conversationApiClient.createConversation(conversationRequest);
        System.out.println(conversation.getName());
    }

    /**
     * Tests updating the name of an existing conversation.
     */
    @Test
    public void testChangeConversationName() throws IOException, InterruptedException {
        conversation.setId(3);
        conversationUpdateRequest = new ConversationUpdateRequest("prööt");
        conversation = conversationApiClient.changeConversationName(conversationUpdateRequest, conversation);
        System.out.println(conversation.getParticipants());
        for (User p : conversation.getParticipants()) {
            System.out.println(p.getUsername());
        }
    }

    /**
     * Tests adding a new user (contact) to a conversation.
     */
    @Test
    public void testAddUserToConversation() throws IOException, InterruptedException {
        Contact contact = new Contact();
        contact.setContactId(4);
        conversation.setId(4);
        conversationApiClient.addUserToConversation(conversation, contact);
    }

    /**
     * Tests a user leaving a conversation.
     */
    @Test
    public void testLeaveConversation() throws IOException, InterruptedException {
        user.setId(4);
        conversation.setId(4);
        conversationApiClient.leaveConversation(conversation, user);
    }

    /**
     * Tests deleting a conversation by its creator.
     */
    @Test
    public void testDeleteConversation() throws IOException, InterruptedException {
        user.setId(1);
        conversation.setId(5);
        conversationApiClient.deleteConversation(conversation, user);
    }

    /**
     * Tests removing a user from a conversation.
     */
    @Test
    public void testRemoveUserFromConversation() throws IOException, InterruptedException {
        conversation.setId(3);
        user.setUserId(3);
        conversationApiClient.removeUserFromConversation(conversation, user);
    }

    /**
     * Tests fetching all conversations that a user participates in.
     */
    @Test
    public void testGetConversationByUserId() throws IOException, InterruptedException {
        user.setId(1);
        List<Conversation> conversations = conversationApiClient.getConversationsById(user);
        for (Conversation c : conversations) {
            System.out.println("Conversation ID: " + c.getId());
            System.out.println("Conversation Type:: " + c.getType());
            System.out.println("Conversation Name:: " + c.getName());
            System.out.println("Conversation Created By: " + c.getCreatedBy());
            System.out.println("Conversation Created At: " + c.getCreatedAt());
            for (User u : c.getParticipants()) {
                System.out.println(" ");
                System.out.println("userId: " + u.getUserId());
                System.out.println("username: " + u.getUsername());
                System.out.println("role: " + u.getRole());
            }
            System.out.println(" ");
        }
    }

    /**
     * Tests fetching all conversations in the system.
     */
    @Test
    public void testGetAllConversations() throws IOException, InterruptedException {
        List<Conversation> conversations = conversationApiClient.getAllConversations();
        for (Conversation c : conversations) {
            System.out.println("Conversation ID: " + c.getId());
            System.out.println("Conversation Type:: " + c.getType());
            System.out.println("Conversation Name:: " + c.getName());
            System.out.println("Conversation Created By: " + c.getCreatedBy());
            System.out.println("Conversation Created At: " + c.getCreatedAt());
            System.out.println("");
        }
    }
}

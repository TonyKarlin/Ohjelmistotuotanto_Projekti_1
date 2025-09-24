import model.Conversation;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ConversationUpdateRequest;
import request.LoginRequest;
import service.ConversationApiClient;
import service.MessageApiClient;
import service.UserApiClient;

import java.io.IOException;
import java.util.List;

public class ConversationApiClientTest {

    private MessageApiClient messageApiClient;
    private UserApiClient userApiClient;
    private LoginRequest loginRequest;
    private Conversation conversation;
    private ConversationApiClient conversationApiClient;
    ConversationUpdateRequest conversationUpdateRequest;

    @BeforeEach
    public void setUp() throws Exception {
        messageApiClient = new MessageApiClient();
        userApiClient = new UserApiClient();
        loginRequest = new LoginRequest();
        conversation = new Conversation();
        conversationApiClient = new ConversationApiClient();
        conversationUpdateRequest = new ConversationUpdateRequest();

    }

    @Test
    public void testGetConversationByUserId() throws IOException, InterruptedException {
        User user = new User();
        user.setId(1);
        List <Conversation> conversations = conversationApiClient.getConversationsById(user);
        for (Conversation c : conversations) {
            System.out.println("Conversation ID: " + c.getId());
            System.out.println("Conversation Type:: " + c.getType());
            System.out.println("Conversation Name:: " + c.getName());
            System.out.println("Conversation Created By: " + c.getCreatedBy());
            System.out.println("Conversation Created At: " + c.getCreatedAt());
            System.out.println("");
    }}

    @Test
    public void testChangeConversationName() throws IOException, InterruptedException {
        conversation.setId(2);
        conversationUpdateRequest = new ConversationUpdateRequest("prööt");
        conversation= conversationApiClient.changeConversationName(conversationUpdateRequest,conversation);
        System.out.println(conversation.getName());

    }

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



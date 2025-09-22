
import model.Conversation;
import model.Message;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.MessageRequest;
import service.MessageApiClient;
import service.UserApiClient;

import java.io.IOException;
import java.util.List;

public class MessageApiClientTest {

    private MessageApiClient messageApiClient;
    private UserApiClient userApiClient;
    private LoginRequest loginRequest;
    private Conversation conversation;

    @BeforeEach
    public void setUp() throws Exception{
        messageApiClient = new MessageApiClient();
        userApiClient = new UserApiClient();
        loginRequest = new LoginRequest();
        conversation = new Conversation();

    }

    //Test to send messages using methods from the frontend
    //Can only perform once because only one user with same username can be existing in the DB
    @Test
    public void testSendMessage() throws IOException {
        MessageRequest request = new MessageRequest();
        conversation.setId(24);
        request.setText("Test Message from JUnit!");
        request.setSenderId(1);
        Message message = messageApiClient.sendMessage(request, conversation);
        System.out.println(message.getText());


    }

    @Test
    public void testGetConversationMessages() throws IOException, InterruptedException {
        conversation.setId(29);
        List<Message> messages = messageApiClient.getConversationMessages(conversation);
        for (Message m : messages) {
            System.out.println(m.getText());
        }
    }

    @Test
    public void testMessageDelete() throws IOException, InterruptedException {
        loginRequest = new LoginRequest("test", "1234");
        User user = userApiClient.loginUser(loginRequest);
        Message message = new Message();
        message.setId(48);
        conversation.setId(20);
        messageApiClient.deleteMessage(conversation, message, user);

    }

}

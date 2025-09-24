
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
    private Message message;
    private MessageRequest messageRequest;

    @BeforeEach
    public void setUp() throws Exception{
        messageApiClient = new MessageApiClient();
        userApiClient = new UserApiClient();
        loginRequest = new LoginRequest();
        conversation = new Conversation();
        message = new Message();
        messageRequest = new MessageRequest();

    }

    //Test to send messages using methods from the frontend
    //Can only perform once because only one user with same username can be existing in the DB
    @Test
    public void testSendMessage() throws IOException {
        conversation.setId(3);
        messageRequest.setText("Test Message from JUnit!");
        messageRequest.setSenderId(1);
        Message message = messageApiClient.sendMessage(messageRequest, conversation);
        System.out.println(message.getText());


    }

    @Test
    public void testGetConversationMessages() throws IOException, InterruptedException {
        conversation.setId(3);
        List<Message> messages = messageApiClient.getConversationMessages(conversation);
        for (Message m : messages) {
            System.out.println(m.getText());
        }
    }

    @Test
    public void testModifyMessage() throws IOException, InterruptedException {
        conversation.setId(3);
        message.setId(2);
        String text = "prööt";
        MessageRequest messageRequest = new MessageRequest(conversation.getId(), text, message.getId());
        Message message1 = messageApiClient.modifyMessage(messageRequest);
        if (message1 != null) {
            System.out.println(message1.getMessageAttachments());
            System.out.println(message1.getSenderId());
            System.out.println(message1.getId());
            System.out.println(message1.getCreatedAt());
            System.out.println(message1.getSenderUsername());
            System.out.println(message1.getText());

        }else {
            System.out.println("Message is null");
        }
    }


    @Test
    public void testMessageDelete() throws IOException, InterruptedException {
        loginRequest = new LoginRequest("joni", "1234");
        User user = userApiClient.loginUser(loginRequest);
        Message message = new Message();
        message.setId(1);
        conversation.setId(3);
        messageApiClient.deleteMessage(conversation, message, user);

    }

}

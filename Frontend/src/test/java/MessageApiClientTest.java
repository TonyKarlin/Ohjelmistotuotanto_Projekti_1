import dto.LoginResponse;
import model.Message;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.MessageRequest;
import service.MessageApiClient;
import service.UserApiClient;

import java.io.IOException;

public class MessageApiClientTest {

    private MessageApiClient messageApiClient;
    private UserApiClient userApiClient;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() throws Exception{
        messageApiClient = new MessageApiClient();
        userApiClient = new UserApiClient();
        loginRequest = new LoginRequest();
    }

    //Test to send messages using methods from the frontend
    //Can only perform once because only one user with same username can be existing in the DB
    @Test
    public void testSendMessage() throws IOException {
        loginRequest = new LoginRequest("test", "1234");

        User user = userApiClient.loginUser(loginRequest);
        MessageRequest request = new MessageRequest();
        //Make sure that you have users in your DB before sending messages
        request.setParticipantIds(java.util.Arrays.asList(1, 2, 3)); // user id's
        request.setText("Test Message from JUnit!");
        request.setSenderId(user.getId());

        Message message = messageApiClient.sendMessage(request);
        System.out.println("Message ID: " + message.getId());
        System.out.println("Sender Username: " + message.getSenderUsername());
        System.out.println("Text: " + message.getText());
        System.out.println("Created At: " + message.getCreatedAt());
    }

}

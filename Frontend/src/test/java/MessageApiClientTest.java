import model.Message;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.MessageRequest;
import service.MessageApiClient;
import service.UserApiClient;

import java.io.IOException;
import java.net.MalformedURLException;

public class MessageApiClientTest {

    private MessageApiClient messageApiClient;
    private UserApiClient userApiClient;

    @BeforeEach
    public void setUp() throws Exception{
        messageApiClient = new MessageApiClient();
        userApiClient = new UserApiClient();
    }

    //Test to send messages using methods from the frontend
    //Can only perform once because only one user with same username can be existing in the DB
    @Test
    public void testSendMessage() throws IOException {
        //creating new user
        User newUser = new User("test8", "test8@example.com", "password123");
        //Register user and saves the json response to the User object
        User registeredUser = userApiClient.registerUser(newUser);
        System.out.println("User Response: " + registeredUser);

        MessageRequest request = new MessageRequest();
        //Make sure that you have users in your DB before sending messages
        request.setParticipantIds(java.util.Arrays.asList(2,3,4,6,9,10)); // user id's
        request.setText("Test Message from JUnit!");

        Message message = messageApiClient.sendMessage(registeredUser, request);
        for (User participants : message.getConversation().getParticipants()) {
                System.out.println("message: " + message.getText());
                System.out.println("participants ID: " + participants.getId());
                System.out.println("username: "+ participants.getUsername());
        }
    }
}

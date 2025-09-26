
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conversation;
import model.Message;
import model.User;
import request.MessageRequest;
import service.MessageApiClient;

public class MessageApiClientTest {

    private MessageApiClient messageApiClient;

    private Conversation conversation;
    private Message message;
    private MessageRequest messageRequest;
    private User user;

    @BeforeEach
    public void setUp() {
        messageApiClient = new MessageApiClient();
        conversation = new Conversation();
        message = new Message();
        messageRequest = new MessageRequest();
        user = new User();
    }

    /**
     * Tests sending a new message to a conversation. Requires an existing
     * conversation and a valid sender. ⚠ Can only be executed once per unique
     * user since usernames must be unique in the DB.
     */
    // @Test
    // public void testSendMessage() {
    //     messageRequest.setConversationId(3);
    //     messageRequest.setText("Test Message from JUnit!");
    //     messageRequest.setSenderId(1);
    //     Message message = messageApiClient.sendMessage(messageRequest);
    //     System.out.println(message.getText());
    // }
    /**
     * Tests fetching all messages from a conversation.
     */
    // @Test
    // public void testGetConversationMessages() throws IOException, InterruptedException {
    //     conversation.setId(12);
    //     List<Message> messages = messageApiClient.getConversationMessages(conversation);
    //     for (Message m : messages) {
    //         System.out.println(m.getId());
    //         System.out.println(m.getSenderId());
    //         System.out.println(m.getSenderUsername());
    //         System.out.println(m.getCreatedAt());
    //         System.out.println(m.getMessageAttachments());
    //     }
    // }
    /**
     * Tests modifying an existing message in a conversation.
     */
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
        } else {
            System.out.println("Message is null");
        }
    }

    /**
     * Tests deleting a message from a conversation.
     */
    @Test
    public void testMessageDelete() throws IOException, InterruptedException {
        user.setId(1);
        message.setId(3);
        conversation.setId(3);
        MessageRequest messageRequest1 = new MessageRequest(user.getId(), message.getId(), conversation.getId());
        messageApiClient.deleteMessage(messageRequest1);
    }
}

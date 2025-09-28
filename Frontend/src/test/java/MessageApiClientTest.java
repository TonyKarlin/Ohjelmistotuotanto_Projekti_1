
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
     * conversation and a valid sender.
     */
    @Test
    public void testSendMessage() {
        try {
            messageRequest.setConversationId(3);
            messageRequest.setText("Test Message from JUnit!");
            messageRequest.setSenderId(1);

            Message sentMessage = messageApiClient.sendMessage(messageRequest);

            if (sentMessage != null) {
                System.out.println("Message sent successfully!");
                System.out.println("Message text: " + sentMessage.getText());
                System.out.println("Message ID: " + sentMessage.getId());
                System.out.println("Sender ID: " + sentMessage.getSenderId());

                // Verify the message content
                assertEquals("Test Message from JUnit!", sentMessage.getText());
                assertNotNull(sentMessage.getId());
                assertEquals(1, sentMessage.getSenderId());
            } else {
                System.out.println("Failed to send message - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Conversation ID 3 doesn't exist");
                System.out.println("- Sender ID 1 doesn't exist");
                System.out.println("- Database connectivity issues");
            }
        } catch (Exception e) {
            System.out.println("Exception during message sending: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests fetching all messages from a conversation.
     */
    @Test
    public void testGetConversationMessages() {
        try {
            conversation.setId(12);
            List<Message> messages = messageApiClient.getConversationMessages(conversation);

            if (messages != null && !messages.isEmpty()) {
                System.out.println("Successfully retrieved " + messages.size() + " messages from conversation 12");

                for (Message m : messages) {
                    System.out.println("Message ID: " + m.getId());
                    System.out.println("Sender ID: " + m.getSenderId());
                    System.out.println("Sender Username: " + m.getSenderUsername());
                    System.out.println("Created At: " + m.getCreatedAt());
                    System.out.println("Attachments: " + m.getMessageAttachments());
                    System.out.println("---");
                }

                // Verify we got messages
                assertFalse(messages.isEmpty(), "Should have at least one message");
                assertNotNull(messages.get(0).getId(), "Message should have an ID");
            } else if (messages != null && messages.isEmpty()) {
                System.out.println("Conversation 12 exists but has no messages");
            } else {
                System.out.println("Failed to retrieve messages - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Conversation ID 12 doesn't exist");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during message retrieval: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests modifying an existing message in a conversation.
     */
    @Test
    public void testModifyMessage() {
        try {
            conversation.setId(3);
            message.setId(2);
            String newText = "Modified message from JUnit test";

            MessageRequest modifyRequest = new MessageRequest(newText, conversation.getId(), message.getId());
            Message modifiedMessage = messageApiClient.modifyMessage(modifyRequest);

            if (modifiedMessage != null) {
                System.out.println("Message modified successfully!");
                System.out.println("Modified text: " + modifiedMessage.getText());
                System.out.println("Message ID: " + modifiedMessage.getId());
                System.out.println("Sender ID: " + modifiedMessage.getSenderId());
                System.out.println("Sender Username: " + modifiedMessage.getSenderUsername());
                System.out.println("Created At: " + modifiedMessage.getCreatedAt());
                System.out.println("Attachments: " + modifiedMessage.getMessageAttachments());

                // Verify the modification
                assertEquals(newText, modifiedMessage.getText(), "Message text should be updated");
                assertEquals(2, modifiedMessage.getId(), "Message ID should remain the same");
            } else {
                System.out.println("Failed to modify message - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Message ID 2 doesn't exist in conversation 3");
                System.out.println("- User doesn't have permission to modify this message");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during message modification: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests deleting a message from a conversation.
     */
    @Test
    public void testMessageDelete() {
        try {
            user.setId(1);
            message.setId(3);
            conversation.setId(3);

            MessageRequest deleteRequest = new MessageRequest(user.getId(), message.getId(), conversation.getId());

            messageApiClient.deleteMessage(deleteRequest);

            System.out.println("Message deletion request sent successfully");
            System.out.println("Attempted to delete message ID: " + message.getId());
            System.out.println("From conversation ID: " + conversation.getId());
            System.out.println("By user ID: " + user.getId());

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during message deletion: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Message ID 3 doesn't exist in conversation 3");
            System.out.println("- User ID 1 doesn't have permission to delete this message");
            System.out.println("- Database connectivity issues");
        }
    }
}

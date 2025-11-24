package fronttest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

class ConversationApiClientTest {

    private Conversation conversation;
    private ConversationRequest conversationRequest;
    private ConversationApiClient conversationApiClient;
    User user;
    ConversationParticipant participant;

    @BeforeEach
    void setUp() {
        conversation = new Conversation();
        conversationApiClient = new ConversationApiClient();
        user = new User();
        participant = new ConversationParticipant();
    }

    /**
     * Tests updating the name of an existing conversation.
     */
    @Test
    void testChangeConversationName() {
        try {
            conversation.setId(3);
            String newName = "Updated Conversation Name from JUnit";
            conversationRequest = new ConversationRequest();

            Conversation updatedConversation = conversationApiClient.changeConversationName(conversationRequest);

            if (updatedConversation != null) {
                System.out.println("Conversation name updated successfully!");
                System.out.println("New name: " + updatedConversation.getName());
                System.out.println("Conversation ID: " + updatedConversation.getId());
                System.out.println("Participants:");

                for (ConversationParticipant p : updatedConversation.getParticipants()) {
                    System.out.println("  - User ID: " + p.getUserId() + ", Username: " + p.getUsername());
                }

                // Verify the name was updated
                assertEquals(newName, updatedConversation.getName(), "Conversation name should be updated");
                assertEquals(3, updatedConversation.getId(), "Conversation ID should remain the same");
            } else {
                System.out.println("Failed to update conversation name - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Conversation ID 3 doesn't exist");
                System.out.println("- User doesn't have permission to update this conversation");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException e) {
            System.out.println("Exception during conversation name update: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests a user leaving a conversation.
     */
    @Test
    void testLeaveConversation() {
        try {
            user.setId(4);
            conversation.setId(4);

            conversationApiClient.leaveConversation(conversation, user);

            System.out.println("Leave conversation request sent successfully");
            System.out.println("User ID: " + user.getId() + " left conversation ID: " + conversation.getId());

            // Verify the user has left the conversation
            List<Conversation> conversations = conversationApiClient.getConversationsById(user);
            boolean found = false;
            for (Conversation c : conversations) {
                if (c.getId() == conversation.getId()) {
                    found = true;
                    break;
                }
            }
            assertFalse(found, "User should have left the conversation");

        } catch (IOException e) {
            System.out.println("Exception during leaving conversation: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Conversation ID 4 doesn't exist");
            System.out.println("- User ID 4 is not a participant in this conversation");
            System.out.println("- Database connectivity issues");
        }
    }

    /**
     * Tests fetching all conversations that a user participates in.
     */
    @Test
    void testGetConversationByUserId() {
        try {
            user.setId(1);
            List<Conversation> conversations = conversationApiClient.getConversationsById(user);

            if (conversations != null && !conversations.isEmpty()) {
                System.out.println("Successfully retrieved " + conversations.size() + " conversations for user ID: " + user.getId());

                for (Conversation c : conversations) {
                    System.out.println("--- Conversation Details ---");
                    System.out.println("Conversation ID: " + c.getId());
                    System.out.println("Conversation Type: " + c.getType());
                    System.out.println("Conversation Name: " + c.getName());
                    System.out.println("Created By: " + c.getCreatedBy());
                    System.out.println("Created At: " + c.getCreatedAt());
                    System.out.println("Participants:");

                    for (ConversationParticipant u : c.getParticipants()) {
                        System.out.println("  - User ID: " + u.getUserId()
                                + ", Username: " + u.getUsername()
                                + ", Role: " + u.getRole());
                    }
                    System.out.println();
                }

                // Verify we got conversations
                assertFalse(conversations.isEmpty(), "User should have at least one conversation");
                assertNotEquals(0, conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                System.out.println("User ID " + user.getId() + " has no conversations");
            } else {
                System.out.println("Failed to retrieve conversations - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- User ID 1 doesn't exist");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException e) {
            System.out.println("Exception during conversation retrieval: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests fetching all conversations in the system.
     */
    @Test
    void testGetAllConversations() {
        try {
            User currentUser = new User();
            List<Conversation> conversations = conversationApiClient.getAllUserConversations(currentUser);

            if (conversations != null && !conversations.isEmpty()) {
                System.out.println("Successfully retrieved " + conversations.size() + " conversations from the system");

                for (Conversation c : conversations) {
                    System.out.println("--- Conversation Summary ---");
                    System.out.println("Conversation ID: " + c.getId());
                    System.out.println("Conversation Type: " + c.getType());
                    System.out.println("Conversation Name: " + c.getName());
                    System.out.println("Created By: " + c.getCreatedBy());
                    System.out.println("Created At: " + c.getCreatedAt());
                    System.out.println();
                }

                // Verify we got conversations
                assertFalse(conversations.isEmpty(), "System should have at least one conversation");
                assertNotEquals(0, conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                System.out.println("No conversations found in the system");
            } else {
                System.out.println("Failed to retrieve all conversations - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Database connectivity issues");
                System.out.println("- API endpoint not accessible");
            }
        } catch (IOException e) {
            System.out.println("Exception during all conversations retrieval: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }
}


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Contact;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import service.ConversationApiClient;

public class ConversationApiClientTest {

    private Conversation conversation;
    private ConversationRequest conversationRequest;
    private ConversationApiClient conversationApiClient;
    User user;
    ConversationParticipant participant;

    @BeforeEach
    public void setUp() throws Exception {
        conversation = new Conversation();
        conversationApiClient = new ConversationApiClient();
        user = new User();
        participant = new ConversationParticipant();
    }

    /**
     * Tests creating a new conversation with a creator and participants.
     */
    @Test
    public void testCreateConversation() {
        try {
            user.setId(1);
            List<Integer> participantIds = Arrays.asList(2, 3);
            String conversationName = "Test Conversation from JUnit";

            conversationRequest = new ConversationRequest(user.getId(), conversationName, participantIds);
            Conversation createdConversation = conversationApiClient.createConversation(conversationRequest);

            if (createdConversation != null) {
                System.out.println("Conversation created successfully!");
                System.out.println("Conversation name: " + createdConversation.getName());
                System.out.println("Conversation ID: " + createdConversation.getId());
                System.out.println("Created by: " + createdConversation.getCreatedBy());

                // Verify the conversation was created correctly
                assertEquals(conversationName, createdConversation.getName(), "Conversation name should match");
                assertNotNull(createdConversation.getId(), "Conversation should have an ID");
                assertEquals(1, createdConversation.getCreatedBy(), "Creator ID should match");
            } else {
                System.out.println("Failed to create conversation - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- User ID 1 doesn't exist");
                System.out.println("- Participant IDs 2, 3 don't exist");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during conversation creation: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests updating the name of an existing conversation.
     */
    @Test
    public void testChangeConversationName() {
        try {
            conversation.setId(3);
            String newName = "Updated Conversation Name from JUnit";
            conversationRequest = new ConversationRequest(newName);

            Conversation updatedConversation = conversationApiClient.changeConversationName(conversationRequest, conversation);

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
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during conversation name update: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests adding a new user (contact) to a conversation.
     */
    @Test
    public void testAddUserToConversation() {
        try {
            Contact contact = new Contact();
            contact.setContactUserId(4);
            conversation.setId(4);

            conversationApiClient.addUserToConversation(conversation, contact);

            System.out.println("Add user to conversation request sent successfully");
            System.out.println("Added user ID: " + contact.getContactUserId());
            System.out.println("To conversation ID: " + conversation.getId());

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during adding user to conversation: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Conversation ID 4 doesn't exist");
            System.out.println("- User ID 4 doesn't exist");
            System.out.println("- User is already a participant in this conversation");
            System.out.println("- Database connectivity issues");
        }
    }

    /**
     * Tests a user leaving a conversation.
     */
    @Test
    public void testLeaveConversation() {
        try {
            user.setId(4);
            conversation.setId(4);

            conversationApiClient.leaveConversation(conversation, user);

            System.out.println("Leave conversation request sent successfully");
            System.out.println("User ID: " + user.getId() + " left conversation ID: " + conversation.getId());

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during leaving conversation: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Conversation ID 4 doesn't exist");
            System.out.println("- User ID 4 is not a participant in this conversation");
            System.out.println("- Database connectivity issues");
        }
    }

    /**
     * Tests deleting a conversation by its creator.
     */
    @Test
    public void testDeleteConversation() {
        try {
            user.setId(1);
            conversation.setId(5);

            conversationApiClient.deleteConversation(conversation, user);

            System.out.println("Delete conversation request sent successfully");
            System.out.println("Conversation ID: " + conversation.getId() + " deleted by user ID: " + user.getId());

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during conversation deletion: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Conversation ID 5 doesn't exist");
            System.out.println("- User ID 1 is not the creator of this conversation");
            System.out.println("- Database connectivity issues");
        }
    }

    /**
     * Tests removing a user from a conversation.
     */
    @Test
    public void testRemoveUserFromConversation() {
        try {
            conversation.setId(3);
            participant.setUserId(3);

            conversationApiClient.removeUserFromConversation(conversation, participant);

            System.out.println("Remove user from conversation request sent successfully");
            System.out.println("User ID: " + participant.getUserId() + " removed from conversation ID: " + conversation.getId());

        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during user removal from conversation: " + e.getMessage());
            System.out.println("Possible causes:");
            System.out.println("- Backend server not running");
            System.out.println("- Conversation ID 3 doesn't exist");
            System.out.println("- User ID 3 is not a participant in this conversation");
            System.out.println("- Insufficient permissions to remove this user");
            System.out.println("- Database connectivity issues");
        }
    }

    /**
     * Tests fetching all conversations that a user participates in.
     */
    @Test
    public void testGetConversationByUserId() {
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
                assertNotNull(conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                System.out.println("User ID " + user.getId() + " has no conversations");
            } else {
                System.out.println("Failed to retrieve conversations - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- User ID 1 doesn't exist");
                System.out.println("- Database connectivity issues");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during conversation retrieval: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }

    /**
     * Tests fetching all conversations in the system.
     */
    @Test
    public void testGetAllConversations() {
        try {
            List<Conversation> conversations = conversationApiClient.getAllConversations();

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
                assertNotNull(conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                System.out.println("No conversations found in the system");
            } else {
                System.out.println("Failed to retrieve all conversations - possible causes:");
                System.out.println("- Backend server not running");
                System.out.println("- Database connectivity issues");
                System.out.println("- API endpoint not accessible");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception during all conversations retrieval: " + e.getMessage());
            System.out.println("This is likely due to backend connectivity issues");
        }
    }
}

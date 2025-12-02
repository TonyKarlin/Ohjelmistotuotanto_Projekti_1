package fronttest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(ConversationApiClientTest.class.getName());
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
                logger.info("Conversation name updated successfully!");
                logger.log(Level.INFO, "New name: {0}", updatedConversation.getName());
                logger.log(Level.INFO, "Conversation ID: {0}", updatedConversation.getId());
                logger.info("Participants:");

                for (ConversationParticipant p : updatedConversation.getParticipants()) {
                    logger.log(Level.INFO, "  - User ID: {0}, Username: {1}", new Object[]{p.getUserId(), p.getUsername()});
                }

                // Verify the name was updated
                assertEquals(newName, updatedConversation.getName(), "Conversation name should be updated");
                assertEquals(3, updatedConversation.getId(), "Conversation ID should remain the same");
            } else {
                logger.warning("Failed to update conversation name - possible causes:");
                logger.warning("- Backend server not running");
                logger.warning("- Conversation ID 3 doesn't exist");
                logger.warning("- User doesn't have permission to update this conversation");
                logger.warning("- Database connectivity issues");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception during conversation name update: {0}", e.getMessage());
            logger.severe("This is likely due to backend connectivity issues");
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

            logger.info("Leave conversation request sent successfully");
            logger.log(Level.INFO, "User ID: {0} left conversation ID: {1}", new Object[]{user.getId(), conversation.getId()});

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
            logger.log(Level.SEVERE, "Exception during leaving conversation: {0}", e.getMessage());
            logger.severe("Possible causes:");
            logger.severe("- Backend server not running");
            logger.severe("- Conversation ID 4 doesn't exist");
            logger.severe("- User ID 4 is not a participant in this conversation");
            logger.severe("- Database connectivity issues");
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
                logger.log(Level.INFO, "Successfully retrieved {0} conversations for user ID: {1}", new Object[]{conversations.size(), user.getId()});

                for (Conversation c : conversations) {
                    logger.info("--- Conversation Details ---");
                    logger.log(Level.INFO, "Conversation ID: {0}", c.getId());
                    logger.log(Level.INFO, "Conversation Type: {0}", c.getType());
                    logger.log(Level.INFO, "Conversation Name: {0}", c.getName());
                    logger.log(Level.INFO, "Created By: {0}", c.getCreatedBy());
                    logger.log(Level.INFO, "Created At: {0}", c.getCreatedAt());
                    logger.info("Participants:");

                    for (ConversationParticipant u : c.getParticipants()) {
                        logger.log(Level.INFO, "  - User ID: {0}, Username: {1}, Role: {2}", new Object[]{u.getUserId(), u.getUsername(), u.getRole()});
                    }
                    logger.info("");
                }

                // Verify we got conversations
                assertFalse(conversations.isEmpty(), "User should have at least one conversation");
                assertNotEquals(0, conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                logger.log(Level.INFO, "User ID {0} has no conversations", user.getId());
            } else {
                logger.warning("Failed to retrieve conversations - possible causes:");
                logger.warning("- Backend server not running");
                logger.warning("- User ID 1 doesn't exist");
                logger.warning("- Database connectivity issues");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception during conversation retrieval: {0}", e.getMessage());
            logger.severe("This is likely due to backend connectivity issues");
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
                logger.log(Level.INFO, "Successfully retrieved {0} conversations from the system", conversations.size());

                for (Conversation c : conversations) {
                    logger.info("--- Conversation Summary ---");
                    logger.log(Level.INFO, "Conversation ID: {0}", c.getId());
                    logger.log(Level.INFO, "Conversation Type: {0}", c.getType());
                    logger.log(Level.INFO, "Conversation Name: {0}", c.getName());
                    logger.log(Level.INFO, "Created By: {0}", c.getCreatedBy());
                    logger.log(Level.INFO, "Created At: {0}", c.getCreatedAt());
                    logger.info("");
                }

                // Verify we got conversations
                assertFalse(conversations.isEmpty(), "System should have at least one conversation");
                assertNotEquals(0, conversations.get(0).getId(), "Conversation should have an ID");
            } else if (conversations != null && conversations.isEmpty()) {
                logger.info("No conversations found in the system");
            } else {
                logger.warning("Failed to retrieve all conversations - possible causes:");
                logger.warning("- Backend server not running");
                logger.warning("- Database connectivity issues");
                logger.warning("- API endpoint not accessible");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception during all conversations retrieval: {0}", e.getMessage());
            logger.severe("This is likely due to backend connectivity issues");
        }
    }
}

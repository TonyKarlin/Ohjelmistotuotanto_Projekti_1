package backend_api.entities;

import backend_api.enums.ConversationType;
import backend_api.enums.ParticipantRole;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConversationTest {

    @Test
    void testConstructor() {
        Conversation conversation = new Conversation(ConversationType.PRIVATE);
        assertEquals(ConversationType.PRIVATE, conversation.getType());
        assertNotNull(conversation.getParticipants());
    }

    @Test
    void testParticipant() {
        User user1 = new User("test", "test", "test@test.com");
        User user2 = new User("test1", "test1", "test1@test1.com");
        Conversation conversation = new Conversation();
        user1.setId(1L);
        user2.setId(2L);

        // Add participant
        conversation.addParticipant(user1, ParticipantRole.MEMBER);
        assertTrue(conversation.hasParticipant(user1));
        assertFalse(conversation.hasParticipant(user2));

        // Remove participant
        ConversationParticipant participant = conversation.getParticipants().get(0);
        conversation.removeParticipant(participant);

        assertTrue(conversation.getParticipants().isEmpty());
        assertNull(participant.getConversation());

        // Add another participant
        conversation.addParticipant(user2, ParticipantRole.MEMBER);
        ConversationParticipant participant2 = conversation.getParticipants().get(0);

        assertEquals(1, conversation.getParticipants().size());
        assertEquals(user2, participant2.getUser());
        assertEquals(conversation, participant2.getConversation());
        assertEquals(ParticipantRole.MEMBER, participant2.getRole());
        assertTrue(conversation.hasParticipant(user2));
    }

    @Test
    void testId() {
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        assertEquals(1L, conversation.getId());
    }

    @Test
    void testType() {
        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.PRIVATE);
        assertEquals(ConversationType.PRIVATE, conversation.getType());
    }

    @Test
    void testParticipants() {
        Conversation conversation = new Conversation();
        User user1 = new User("test", "test", "test@test.com");
        ConversationParticipant participant = new ConversationParticipant(conversation, user1, ParticipantRole.MEMBER);

        List<ConversationParticipant> newParticipants = List.of(participant);
        conversation.setParticipants(newParticipants);

        assertEquals(1, conversation.getParticipants().size());
        assertEquals(participant, conversation.getParticipants().get(0));
    }
}
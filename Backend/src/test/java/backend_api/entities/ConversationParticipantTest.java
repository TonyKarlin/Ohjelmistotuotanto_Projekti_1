package backend_api.entities;

import backend_api.enums.ParticipantRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConversationParticipantTest {

    @Test
    void testId() {
        ConversationParticipant participant = new ConversationParticipant();
        ConversationParticipantId id = new ConversationParticipantId();
        participant.setId(id);
        assertEquals(id, participant.getId());
    }

    @Test
    void testUser() {
        ConversationParticipant participant = new ConversationParticipant();
        User user = new User("test", "test", "test@test.com");
        participant.setUser(user);
        assertEquals(user, participant.getUser());
    }

    @Test
    void testConversation() {
        ConversationParticipant participant = new ConversationParticipant();
        Conversation conversation = new Conversation();
        participant.setConversation(conversation);
        assertEquals(conversation, participant.getConversation());
    }

    @Test
    void testRole() {
        ConversationParticipant participant = new ConversationParticipant();
        participant.setRole(ParticipantRole.ADMIN);
        assertEquals(ParticipantRole.ADMIN, participant.getRole());
    }

    @Test
    void testParticipant() {
        ConversationParticipant participant = new ConversationParticipant();
        User user = new User("test", "test", "test@test.com");
        User user1 = new User("test1", "test1", "test@test.com");
        participant.setUser(user);
        assertTrue(participant.isParticipant(user));
        assertFalse(participant.isParticipant(user1));
    }

    @Test
    void testJoinedAt() {
        ConversationParticipant participant = new ConversationParticipant();
        LocalDateTime now = LocalDateTime.now();
        participant.setJoinedAt(now);
        assertEquals(now, participant.getJoinedAt());
    }

    @Test
    void testConstructor() {
        Conversation conversation = new Conversation();
        User user = new User("test", "test", "test@test.com");
        ConversationParticipant participant = new ConversationParticipant(conversation, user, ParticipantRole.MEMBER);
        assertEquals(conversation, participant.getConversation());
        assertEquals(user, participant.getUser());
        assertEquals(ParticipantRole.MEMBER, participant.getRole());
        assertNotNull(participant.getJoinedAt());
    }
}
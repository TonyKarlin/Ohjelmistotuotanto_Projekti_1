package backend_api.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationParticipantIdTest {

    @Test
    void testConstructor() {
        ConversationParticipantId id = new ConversationParticipantId(1L, 2L);

        assertEquals(1L, id.getConversationId());
        assertEquals(2L, id.getUserId());
    }

    @Test
    void testConversationId() {
        ConversationParticipantId id = new ConversationParticipantId(1L, 2L);
        id.setConversationId(5L);
        assertEquals(5L, id.getConversationId());
    }

    @Test
    void testUserId() {
        ConversationParticipantId id = new ConversationParticipantId(1L, 2L);
        id.setUserId(5L);
        assertEquals(5L, id.getUserId());
    }

    @Test
    void testEquals() {
        ConversationParticipantId id1 = new ConversationParticipantId(1L, 2L);
        ConversationParticipantId id2 = new ConversationParticipantId(1L, 2L);
        ConversationParticipantId id3 = new ConversationParticipantId(2L, 3L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);

        assertNotEquals(id1, null);
        assertNotEquals(id1, new Object());
    }

    @Test
    void testHashCode() {
        ConversationParticipantId id1 = new ConversationParticipantId(1L, 2L);
        ConversationParticipantId id2 = new ConversationParticipantId(1L, 2L);

        assertEquals(id1.hashCode(), id2.hashCode());
    }
}
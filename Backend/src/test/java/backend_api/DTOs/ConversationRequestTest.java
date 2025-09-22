package backend_api.DTOs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationRequestTest {

    @Test
    void creatorId() {
        ConversationRequest request = new ConversationRequest();
        request.setCreatorId(1L);
        assertEquals(1L, request.getCreatorId());
    }

    @Test
    void participantIds() {
        ConversationRequest request = new ConversationRequest();
        request.setParticipantIds(java.util.Arrays.asList(1L, 2L, 3L));
        assertEquals(java.util.Arrays.asList(1L, 2L, 3L), request.getParticipantIds());
    }

    @Test
    void name() {
        ConversationRequest request = new ConversationRequest();
        request.setName("Test");
        assertEquals("Test", request.getName());
    }
}
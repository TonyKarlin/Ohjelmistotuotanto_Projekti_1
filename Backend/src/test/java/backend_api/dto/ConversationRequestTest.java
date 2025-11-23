package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import backend_api.dto.conversations.ConversationRequest;

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

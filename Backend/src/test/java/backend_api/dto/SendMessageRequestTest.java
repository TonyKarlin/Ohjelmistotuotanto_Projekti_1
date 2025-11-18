package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import backend_api.dto.messages.SendMessageRequest;

class SendMessageRequestTest {

    @Test
    void conversationExists() {
        SendMessageRequest request = new SendMessageRequest();
        request.setConversationId(1L);

        assertTrue(request.conversationExists());

        SendMessageRequest request1 = new SendMessageRequest();
        assertFalse(request1.conversationExists());
    }

    @Test
    void hasAttachment() {
        SendMessageRequest request = new SendMessageRequest();
        assertFalse(request.hasAttachment());
        request.setFileData(new byte[]{1, 2, 3});
        request.setFileType("image/png");
        assertTrue(request.hasAttachment());
    }

    @Test
    void senderId() {
        SendMessageRequest request = new SendMessageRequest();
        request.setSenderId(1L);
        assertEquals(1L, request.getSenderId());
    }

    @Test
    void conversationId() {
        SendMessageRequest request = new SendMessageRequest();
        request.setConversationId(1L);
        assertEquals(1L, request.getConversationId());
    }

    @Test
    void participantIds() {
        SendMessageRequest request = new SendMessageRequest();
        request.setParticipantIds(java.util.Arrays.asList(1L, 2L, 3L));
        assertEquals(java.util.Arrays.asList(1L, 2L, 3L), request.getParticipantIds());
    }

    @Test
    void text() {
        SendMessageRequest request = new SendMessageRequest();
        request.setText("Hello");
        assertEquals("Hello", request.getText());
    }

    @Test
    void fileData() {
        SendMessageRequest request = new SendMessageRequest();
        byte[] data = new byte[]{1, 2, 3};
        request.setFileData(data);
        assertArrayEquals(data, request.getFileData());
    }

    @Test
    void fileType() {
        SendMessageRequest request = new SendMessageRequest();
        request.setFileType("image/png");
        assertEquals("image/png", request.getFileType());
    }
}

package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import backend_api.dto.messages.MessageContentDTO;
import backend_api.entities.MessageContent;

class MessageContentDTOTest {

    @Test
    void fromMessageContentEntity() {
        MessageContent messageContent = new MessageContent();
        messageContent.setId(1L);
        messageContent.setFileType("image/png");

        MessageContentDTO dto = MessageContentDTO.fromMessageContentEntity(messageContent);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("image/png", dto.getFileType());
    }

    @Test
    void id() {
        MessageContentDTO dto = new MessageContentDTO();
        dto.setId(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void fileType() {
        MessageContentDTO dto = new MessageContentDTO();
        dto.setFileType("image/png");
        assertEquals("image/png", dto.getFileType());
    }
}

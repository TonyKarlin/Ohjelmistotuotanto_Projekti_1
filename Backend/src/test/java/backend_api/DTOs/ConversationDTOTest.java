package backend_api.DTOs;

import backend_api.DTOs.conversations.ConversationDTO;
import backend_api.entities.Conversation;
import backend_api.enums.ConversationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConversationDTOTest {

    @Test
    void conversationEntity() {
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setType(ConversationType.GROUP);
        conversation.setName("Test");
        conversation.setCreatedBy(1L);
        LocalDateTime now = LocalDateTime.now();
        conversation.setCreatedAt(now);
        ConversationDTO dto = ConversationDTO.fromConversationEntity(conversation);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getCreatedBy());
        assertEquals("GROUP", dto.getType());
        assertEquals("Test", dto.getName());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void id() {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void type() {
        ConversationDTO dto = new ConversationDTO();
        dto.setType(ConversationType.GROUP.name());
        assertEquals(ConversationType.GROUP.name(), dto.getType());
    }

    @Test
    void name() {
        ConversationDTO dto = new ConversationDTO();
        dto.setName("Test");
        assertEquals("Test", dto.getName());
    }

    @Test
    void createdBy() {
        ConversationDTO dto = new ConversationDTO();
        dto.setCreatedBy(1L);
        assertEquals(1L, dto.getCreatedBy());
    }

    @Test
    void createdAt() {
        ConversationDTO dto = new ConversationDTO();
        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedAt(now);
        assertEquals(now, dto.getCreatedAt());
    }
}
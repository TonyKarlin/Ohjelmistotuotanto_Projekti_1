package backend_api.DTOs;

import backend_api.DTOs.messages.MessageContentDTO;
import backend_api.DTOs.messages.MessageDTO;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.MessageContent;
import backend_api.entities.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class MessageDTOTest {

    @Test
    void fromMessageEntity() {
        Conversation conversation = new Conversation();
        conversation.setId(1L);

        User sender = new User();
        sender.setId(1L);
        sender.setUsername("Test");

        MessageContent content = new MessageContent();
        content.setId(1L);
        content.setFileType("txt");

        MessageContent content1 = new MessageContent();
        content1.setId(2L);
        content1.setFileType("jpg");

        Message message = new Message();
        message.setId(1L);
        message.setConversation(conversation);
        message.setSender(sender);
        message.setText("TestTest");
        LocalDateTime now = LocalDateTime.now();
        message.setCreatedAt(now);
        message.setAttachments(java.util.Arrays.asList(content, content1));

        MessageDTO dto = MessageDTO.fromMessageEntity(message);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getConversationId());
        assertEquals(1L, dto.getSenderId());
        assertEquals("Test", dto.getSenderUsername());
        assertEquals("TestTest", dto.getText());
        assertEquals(now, dto.getCreatedAt());

        assertNotNull(dto.getAttachments());
        assertEquals(2, dto.getAttachments().size());
        assertEquals(1L, dto.getAttachments().get(0).getId());
        assertEquals("txt", dto.getAttachments().get(0).getFileType());
        assertEquals(2L, dto.getAttachments().get(1).getId());
        assertEquals("jpg", dto.getAttachments().get(1).getFileType());

    }

    @Test
    void id() {
        MessageDTO dto = new MessageDTO();
        dto.setId(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void conversationId() {
        MessageDTO dto = new MessageDTO();
        dto.setConversationId(1L);
        assertEquals(1L, dto.getConversationId());
    }

    @Test
    void senderId() {
        MessageDTO dto = new MessageDTO();
        dto.setSenderId(1L);
        assertEquals(1L, dto.getSenderId());
    }

    @Test
    void senderUsername() {
        MessageDTO dto = new MessageDTO();
        dto.setSenderUsername("Test");
        assertEquals("Test", dto.getSenderUsername());
    }

    @Test
    void text() {
        MessageDTO dto = new MessageDTO();
        dto.setText("Test");
        assertEquals("Test", dto.getText());
    }

    @Test
    void createdAt() {
        MessageDTO dto = new MessageDTO();
        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedAt(now);
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void attachments() {
        MessageDTO dto = new MessageDTO();
        MessageContentDTO content1 = new MessageContentDTO();
        content1.setId(1L);
        content1.setFileType("txt");

        MessageContentDTO content2 = new MessageContentDTO();
        content2.setId(2L);
        content2.setFileType("jpg");

        dto.setAttachments(java.util.Arrays.asList(content1, content2));

        assertNotNull(dto.getAttachments());
        assertEquals(2, dto.getAttachments().size());
        assertEquals(1L, dto.getAttachments().get(0).getId());
        assertEquals("txt", dto.getAttachments().get(0).getFileType());
        assertEquals(2L, dto.getAttachments().get(1).getId());
        assertEquals("jpg", dto.getAttachments().get(1).getFileType());
    }
}
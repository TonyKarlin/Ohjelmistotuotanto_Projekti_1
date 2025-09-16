package backend_api.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testPrePersist() {
        Message message = new Message();
        message.setCreatedAt(null);
        assertNull(message.getCreatedAt());
        message.prePersist();
        assertNotNull(message.getCreatedAt());
        assertTrue(message.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void TestId() {
        Message message = new Message();
        message.setId(1L);
        assertEquals(1L, message.getId());
    }

    @Test
    void TestSender() {
        Message message = new Message();
        User user = new User("test", "test", "test@test.com");
        message.setSender(user);
        assertEquals(user, message.getSender());
    }

    @Test
    void TestConversation() {
        Message message = new Message();
        Conversation conversation = new Conversation();
        message.setConversation(conversation);
        assertEquals(conversation, message.getConversation());
    }

    @Test
    void TestText() {
        Message message = new Message();
        message.setText("Test");
        assertEquals("Test", message.getText());
    }

    @Test
    void TestGetContent() {
        Message message = new Message();
        List<MessageContent> attachments = new ArrayList<>();
        message.setAttachments(attachments);
        assertEquals(attachments, message.getContent());
    }

    @Test
    void TestAttachments() {
        Message message = new Message();
        MessageContent content = new MessageContent();

        message.addAttachments(content);
        assertTrue(message.getContent().contains(content));
        assertEquals(message, content.getMessage());
    }

    @Test
    void CreatedAt() {
        Message message = new Message();
        LocalDateTime now = LocalDateTime.now();
        message.setCreatedAt(now);
        assertEquals(now, message.getCreatedAt());
    }
}
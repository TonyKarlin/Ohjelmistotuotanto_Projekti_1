package backend_api.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testConstructor() {
        User user = new User("test", "test", "test@test.com");
        Conversation conversation = new Conversation();
        MessageContent content = new MessageContent();
        List<MessageContent> attachments = new ArrayList<>();
        attachments.add(content);


        Message message = new Message(user, conversation, attachments);

        message.setSender(user);
        message.setConversation(conversation);
        message.addAttachments(content);

        assertEquals(user, message.getSender());
        assertEquals(conversation, message.getConversation());
        assertTrue(message.getContent().contains(content));

    }

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
    void testId() {
        Message message = new Message();
        message.setId(1L);
        assertEquals(1L, message.getId());
    }

    @Test
    void testSender() {
        Message message = new Message();
        User user = new User("test", "test", "test@test.com");
        message.setSender(user);
        assertEquals(user, message.getSender());
    }

    @Test
    void testConversation() {
        Message message = new Message();
        Conversation conversation = new Conversation();
        message.setConversation(conversation);
        assertEquals(conversation, message.getConversation());
    }

    @Test
    void testText() {
        Message message = new Message();
        message.setText("Test");
        assertEquals("Test", message.getText());
    }

    @Test
    void testGetContent() {
        Message message = new Message();
        List<MessageContent> attachments = new ArrayList<>();
        message.setAttachments(attachments);
        assertEquals(attachments, message.getContent());
    }

    @Test
    void testAttachments() {
        Message message = new Message();
        MessageContent content = new MessageContent();

        message.addAttachments(content);
        assertTrue(message.getContent().contains(content));
        assertEquals(message, content.getMessage());
    }

    @Test
    void testCreatedAt() {
        Message message = new Message();
        LocalDateTime now = LocalDateTime.now();
        message.setCreatedAt(now);
        assertEquals(now, message.getCreatedAt());
    }
}
package backend_api.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageContentTest {

    @Test
    void id() {
        MessageContent content = new MessageContent();
        content.setId(1L);
        assertEquals(1L, content.getId());

    }

    @Test
    void data() {
        MessageContent content = new MessageContent();
        byte[] data = {1, 2, 3};
        content.setData(data);
        assertArrayEquals(data, content.getData());
    }

    @Test
    void filetype() {
        MessageContent content = new MessageContent();
        content.setFileType("image/png");
        assertEquals("image/png", content.getFileType());
    }

    @Test
    void message() {
        MessageContent content = new MessageContent();
        Message message = new Message();
        content.setMessage(message);
        assertEquals(message, content.getMessage());
    }
}
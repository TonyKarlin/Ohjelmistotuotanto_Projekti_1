package backend_api.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageContentTest {

    @Test
    void testConstructor() {
        byte[] data = {1, 2, 3};
        String fileType = "image/png";
        Message message = new Message();
        MessageContent content = new MessageContent(data, fileType, message);
        assertNotNull(content);
        assertArrayEquals(data, content.getData());
        assertEquals(fileType, content.getFileType());
        assertEquals(message, content.getMessage());


    }

    @Test
    void testId() {
        MessageContent content = new MessageContent();
        content.setId(1L);
        assertEquals(1L, content.getId());

    }

    @Test
    void testData() {
        MessageContent content = new MessageContent();
        byte[] data = {1, 2, 3};
        content.setData(data);
        assertArrayEquals(data, content.getData());
    }

    @Test
    void testFiletype() {
        MessageContent content = new MessageContent();
        content.setFileType("image/png");
        assertEquals("image/png", content.getFileType());
    }

    @Test
    void testMessage() {
        MessageContent content = new MessageContent();
        Message message = new Message();
        content.setMessage(message);
        assertEquals(message, content.getMessage());
    }
}
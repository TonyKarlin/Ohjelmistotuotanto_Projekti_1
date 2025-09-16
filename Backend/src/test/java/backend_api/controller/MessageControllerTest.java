package backend_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import backend_api.DTOs.MessageDTO;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Message;
import backend_api.entities.User;
import backend_api.services.MessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class MessageControllerTest {

    @BeforeAll
    static void setUp() {

    }

    @Test
    void sendMessage() {
        MessageService service = mock(MessageService.class);
        MessageController controller = new MessageController(service);

        Message mockMessage = new Message();
        mockMessage.setId(1L); // Setting Message ID to 1

        backend_api.entities.User sender = new User(); // Creates a mock sender
        sender.setId(1L);                              // Cannot be null
        mockMessage.setSender(sender);

        when(service.sendMessage(org.mockito.ArgumentMatchers.any())).thenReturn(mockMessage);
        ResponseEntity<MessageDTO> response = controller.sendMessage(new SendMessageRequest());

        // Asserts
        assertEquals(201, response.getStatusCodeValue(), "Expected HTTP status 201 Created");
        assertEquals(1L, response.getBody().getId(), "Expected message ID to be 1");
    }

    @Test
    void getMessages() {
        MessageService service = mock(MessageService.class);
        MessageController controller = new MessageController(service);

        List<Message> messages = List.of(new Message(), new Message()); // Create two mock messages
        List<User> senders = List.of(new User(), new User()); // Create two mock senders

        // Set up IDs and associations
        senders.get(0).setId(1L);
        senders.get(1).setId(2L);

        messages.get(0).setId(1L);
        messages.get(0).setSender(senders.get(0));
        messages.get(1).setId(2L);
        messages.get(1).setSender(senders.get(1));

        // Mock the service method
        when(service.getMessagesByConversationId(1L)).thenReturn(messages);
        ResponseEntity<List<MessageDTO>> response = controller.getMessages(1L);

        assertEquals(2, response.getBody().size(), "Expected 2 messages in the response");
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 OK");

        assertEquals(1L, response.getBody().get(0).getSenderId(), "Expected sender ID to be 1");
        assertEquals(2L, response.getBody().get(1).getSenderId(), "Expected sender ID to be 2");
    }

    @Test
    void getMessageById() {
        MessageService service = mock(MessageService.class);
        MessageController controller = new MessageController(service);

        Message mockMessage = new Message();
        mockMessage.setId(12L); // Setting Message ID to 12

        backend_api.entities.User sender = new User(); // Creates a mock sender
        sender.setId(1L);                              // Cannot be null
        mockMessage.setSender(sender);

        when(service.getMessageByIdAndConversationId(12L, 8L)).thenReturn(Optional.of(mockMessage));
        ResponseEntity<MessageDTO> response = controller.getMessageById(8L, 12L);

        // Asserts
        assertEquals(200, response.getStatusCodeValue(), "Expected HTTP status 200 OK");
        assertEquals(12L, response.getBody().getId(), "Expected message ID to be 12");
    }
}

package backend_api.controller;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import backend_api.DTOs.messages.MessageDTO;
import backend_api.controller.messaging.MessageController;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.User;
import backend_api.services.MessageService;

public class MessageControllerTest {

//    @Test
//    void sendMessage() {
//        MessageService service = mock(MessageService.class);
//        MessageController controller = new MessageController(service);
//
//        Message mockMessage = new Message();
//        mockMessage.setId(1L); // Setting Message ID to 1
//
//        backend_api.entities.User sender = new User(); // Creates a mock sender
//        sender.setId(1L);                              // Cannot be null
//        mockMessage.setSender(sender);
//
//        when(service.sendMessage(org.mockito.ArgumentMatchers.any())).thenReturn(mockMessage);
//        ResponseEntity<MessageDTO> response = controller.sendMessage(new SendMessageRequest());
//
//        // Asserts
//        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Expected HTTP status 201 Created");
//        assertEquals(1L, response.getBody().getId(), "Expected message ID to be 1");
//    }
//
//    @Test
//    void sendMessage_Failure() {
//        MessageService service = mock(MessageService.class);
//        MessageController controller = new MessageController(service);
//
//        // Simulate failure in sending message
//        when(service.sendMessage(org.mockito.ArgumentMatchers.any())).thenReturn(null);
//        ResponseEntity<MessageDTO> response = controller.sendMessage(new SendMessageRequest());
//
//        // Asserts
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected HTTP status 400 Bad Request");
//        assertNull(response.getBody(), "Expected response body to be null");
//    }
    @Test
    void getMessages() {
        MessageService service = mock(MessageService.class);
        MessageController controller = new MessageController(service);

        List<Message> messages = List.of(new Message(), new Message()); // Create two mock messages
        List<User> senders = List.of(new User(), new User()); // Create two mock senders
        Conversation conversation = new Conversation();

        // Set up IDs and associations
        senders.get(0).setId(1L);
        senders.get(1).setId(2L);

        messages.get(0).setId(1L);
        messages.get(0).setSender(senders.get(0));
        messages.get(1).setId(2L);
        messages.get(1).setSender(senders.get(1));
        messages.get(0).setConversation(conversation);
        messages.get(1).setConversation(conversation);

        // Mock the service method
        when(service.getMessagesByConversationId(1L)).thenReturn(messages);
        ResponseEntity<List<MessageDTO>> response = controller.getMessages(1L);

        assertEquals(2, response.getBody().size(), "Expected 2 messages in the response");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");

        assertEquals(1L, response.getBody().get(0).getSenderId(), "Expected sender ID to be 1");
        assertEquals(2L, response.getBody().get(1).getSenderId(), "Expected sender ID to be 2");
    }

    @Test
    void getMessageById() {
        MessageService service = mock(MessageService.class);
        MessageController controller = new MessageController(service);
        Conversation conversation = new Conversation();
        Message mockMessage = new Message();
        mockMessage.setId(12L);
        conversation.setId(8L);
        mockMessage.setConversation(conversation); // Setting Message

        backend_api.entities.User sender = new User(); // Creates a mock sender
        sender.setId(1L);                              // Cannot be null
        mockMessage.setSender(sender);

        when(service.getMessageByIdAndConversationId(12L, 8L)).thenReturn(Optional.of(mockMessage));
        ResponseEntity<MessageDTO> response = controller.getMessageById(8L, 12L);

        // Asserts
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");
        assertEquals(12L, response.getBody().getId(), "Expected message ID to be 12");
    }

}

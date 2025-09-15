package backend_api.controller;


import backend_api.DTOs.MessageDTO;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.services.ConversationService;
import backend_api.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/conversations")
public class MessageController {
    private final MessageService messageService;
    private final ConversationService conversationService;

    public MessageController(MessageService messageService, ConversationService conversationService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
    }


    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest request) {

        Message message = messageService.sendMessage(request);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable("conversationId") Long conversationId) {
        List<Message> messages = messageService.getMessagesByConversationId(conversationId);

        Optional<Conversation> conversationOpt = conversationService.findById(conversationId);
        if (conversationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MessageDTO> dtos = messages.stream()
                .map(MessageDTO::fromMessageEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable("userId") Long userId, @PathVariable("messageId") Long messageId) {
        boolean result = messageService.deleteMessage(userId, messageId);
        if (result) {
            return ResponseEntity.ok(Map.of("message", "Message deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Message not found or you are not the sender"));
        }
    }

}

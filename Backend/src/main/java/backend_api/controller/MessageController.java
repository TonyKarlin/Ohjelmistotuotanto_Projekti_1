package backend_api.controller;


import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Message;
import backend_api.entities.User;
import backend_api.repository.MessageRepository;
import backend_api.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping
    public ResponseEntity<Message> sendMessage(@PathVariable("userId") Long userId, @RequestBody SendMessageRequest request) {
        request.setSenderId(userId);
        Message message = messageService.sendMessage(request);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable("userId") Long userId, @PathVariable("messageId") Long messageId) {
        boolean result = messageService.deleteMessage(userId, messageId);
        if (result) {
            return ResponseEntity.ok(Map.of("message", "Message deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Message not found or you are not the sender"));
        }
    }

}

package backend_api.controller;


import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Message;
import backend_api.entities.User;
import backend_api.repository.MessageRepository;
import backend_api.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{messageId}")
    public Optional<Message> getMessageById(@PathVariable Long messageId, @PathVariable("userId") Long userId) {
        return messageService.getMessageById(userId, messageId);
    }

    @PostMapping("/send")
    public Message sendMessage(@PathVariable("userId") Long userId, @RequestBody SendMessageRequest request) {
        request.setSenderId(userId);
        return messageService.sendMessage(request);
    }

//    @DeleteMapping("/{messageId}/delete")
//    public void deleteMessage(@PathVariable("userId") Long userId, @PathVariable Long messageId) {
//        messageService.deleteMessage(messageId);
//    }

}

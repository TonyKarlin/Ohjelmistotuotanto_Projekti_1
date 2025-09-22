package backend_api.controller;


import backend_api.DTOs.MessageDTO;
import backend_api.DTOs.MessageResponse;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.services.ConversationService;
import backend_api.services.MessageService;
import backend_api.utils.CustomUserDetails;
import backend_api.utils.customexceptions.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/conversations/{conversationId}/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(
            @PathVariable("conversationId") Long conversationId,
            @RequestBody SendMessageRequest request) {

        request.setConversationId(conversationId);
        Message message = messageService.sendMessage(request);

        if (message == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        MessageDTO dto = MessageDTO.fromMessageEntity(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable("conversationId") Long conversationId) {
        List<Message> messages = messageService.getMessagesByConversationId(conversationId);

        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<MessageDTO> dtos =
                messages.stream()
                        .map(MessageDTO::fromMessageEntity)
                        .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable("conversationId") Long conversationId,
                                                     @PathVariable("messageId") Long messageId) {
        Optional<Message> messageOptional = messageService.getMessageByIdAndConversationId(messageId, conversationId);
        if (messageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MessageDTO dto = MessageDTO.fromMessageEntity(messageOptional.get());
        return ResponseEntity.ok(dto);
    }


    // WIP: Poistetaan viesti, jos käyttäjä on viestin lähettäjä tai keskustelun ylläpitäjä
    // sitten kun roolit mukana kunnolla. Tällä hetkellä vain viestin lähettäjä voi poistaa oman viestinsä.
    // Ei toimi ennen kuin JWT auth on kunnossa.
    @DeleteMapping("/{messageId}")
    public ResponseEntity<MessageResponse> deleteMessage(@PathVariable("conversationId") Long conversationId,
                                                         @PathVariable("messageId") Long messageId,
                                                         @RequestParam(required = false) Long userId) {

        // Temporary user ID for testing if not provided
        if (userId == null) {
            userId = 1L;
        }

        messageService.deleteMessage(userId, messageId, conversationId);
        MessageResponse response = new MessageResponse(
                messageId,
                userId,
                "Message deleted successfully");

        return ResponseEntity.ok(response);
    }
}



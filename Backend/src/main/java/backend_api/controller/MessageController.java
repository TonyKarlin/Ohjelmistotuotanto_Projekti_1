package backend_api.controller;


import backend_api.DTOs.MessageDTO;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.services.ConversationService;
import backend_api.services.MessageService;
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
@RequestMapping("api/conversations")
public class MessageController {
    private final MessageService messageService;
    private final ConversationService conversationService;

    public MessageController(MessageService messageService, ConversationService conversationService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
    }


    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody SendMessageRequest request) {

        Message message = messageService.sendMessage(request);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        MessageDTO dto = MessageDTO.fromMessageEntity(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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


    // WIP: Poistetaan viesti, jos käyttäjä on viestin lähettäjä tai keskustelun ylläpitäjä
    // sitten kun roolit mukana kunnolla
//    @DeleteMapping("/messages/{messageId}")
//    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId,
//                                           @AuthenticationPrincipal UserDetails userDetails) {
//
//        boolean deleted = messageService.deleteMessage(messageId);
//
//        if (deleted) {
//            return ResponseEntity.ok(Map.of("message", "Message deleted successfully"));
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Map.of("error", "Message not found or you are not authorized to delete it"));
//        }
//    }
}

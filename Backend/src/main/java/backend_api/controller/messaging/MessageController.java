package backend_api.controller.messaging;


import backend_api.DTOs.messages.EditMessageTextRequest;
import backend_api.DTOs.messages.MessageDTO;
import backend_api.DTOs.messages.MessageResponse;
import backend_api.DTOs.messages.SendMessageRequest;
import backend_api.entities.Message;
import backend_api.services.MessageService;
import backend_api.utils.customexceptions.BadMessageRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            throw new BadMessageRequestException("Failed to send message. Please check the request data.");
        }

        MessageDTO dto = MessageDTO.fromMessageEntity(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable("conversationId") Long conversationId) {
        List<Message> messages = messageService.getMessagesByConversationId(conversationId);

        if (messages.isEmpty()) {
            throw new BadMessageRequestException("No messages found for this conversation.");
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
            throw new BadMessageRequestException("Message not found in this conversation.");
        }

        MessageDTO dto = MessageDTO.fromMessageEntity(messageOptional.get());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<?> editMessage(@PathVariable("messageId") Long messageId,
                                         @PathVariable("conversationId") Long conversationId,
                                         @RequestBody EditMessageTextRequest request,
                                         @RequestParam(required = false) Long userId) {

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new BadMessageRequestException("Message text cannot be empty");
        }

        Message updatedMessage = messageService.editMessage(conversationId, messageId, userId, request.getText());
        return ResponseEntity.ok(MessageDTO.fromMessageEntity(updatedMessage));
    }


    // WIP: Poistetaan viesti, jos käyttäjä on viestin lähettäjä tai keskustelun ylläpitäjä
    // sitten kun roolit mukana kunnolla. Tällä hetkellä vain viestin lähettäjä voi poistaa oman viestinsä.
    // Ei toimi ennen kuin JWT auth on kunnossa.
    @DeleteMapping("/{messageId}")
    public ResponseEntity<MessageResponse> deleteMessage(@PathVariable("conversationId") Long conversationId,
                                                         @PathVariable("messageId") Long messageId,
                                                         @RequestParam(required = false) Long userId) {


        messageService.deleteMessage(userId, messageId, conversationId);
        MessageResponse response = new MessageResponse(
                messageId,
                userId,
                "Message deleted successfully");

        return ResponseEntity.ok(response);
    }
}



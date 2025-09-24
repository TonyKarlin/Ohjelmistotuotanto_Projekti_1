package backend_api.controller.messaging;


import backend_api.DTOs.conversations.ConversationDTO;
import backend_api.DTOs.conversations.ConversationParticipantResponse;
import backend_api.DTOs.conversations.ConversationRequest;
import backend_api.entities.Conversation;
import backend_api.services.ConversationService;
import backend_api.utils.customexceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationRequest request) {
        Conversation conversation = conversationService.createAConversation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ConversationDTO.fromConversationEntity(conversation));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long conversationId) {
        Conversation conversation = conversationService.getConversationById(conversationId);
        if (conversation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(ConversationDTO.fromConversationEntity(conversation));
    }

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        List<Conversation> conversations = conversationService.getAllConversations();
        List<ConversationDTO> dtos = conversations.stream()
                .map(ConversationDTO::fromConversationEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversationsByUserId(@PathVariable("userId") Long userId) {
        List<Conversation> conversations = conversationService.getConversationsByUserId(userId);
        List<ConversationDTO> dtos = conversations.stream()
                .map(ConversationDTO::fromConversationEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{conversationId}/update")
    public ResponseEntity<?> updateConversation(@PathVariable Long conversationId, @RequestBody ConversationRequest request) {
        Conversation updatedConversation = conversationService.updateConversation(conversationId, request);
        return ResponseEntity.ok(ConversationDTO.fromConversationEntity(updatedConversation));
    }


    @PutMapping("/{conversationId}/participants/{userId}")
    public ResponseEntity<ConversationParticipantResponse> addUserToConversation(@PathVariable Long conversationId,
                                                                                 @PathVariable Long userId) {

        conversationService.addUserToConversation(conversationId, userId);
        ConversationParticipantResponse message = new ConversationParticipantResponse(
                conversationId,
                userId,
                "User added to conversation successfully");

        return ResponseEntity.ok(message);
    }


    @DeleteMapping("/{conversationId}/participants/{userId}")
    public ResponseEntity<?> removeUserFromConversation(
            @PathVariable Long conversationId,
            @PathVariable Long userId) {

        boolean removed = conversationService.removeUserFromConversation(conversationId, userId);

        if (removed) {
            ConversationParticipantResponse response = new ConversationParticipantResponse(
                    conversationId,
                    userId,
                    "User removed from conversation successfully"
            );
            return ResponseEntity.ok(response);
        } else {
            throw new UserNotFoundException("User with id " + userId + " not found in conversation " + conversationId);
        }
    }

    @PatchMapping("/{conversationId}/leave")
    public ResponseEntity<?> leaveConversation(@PathVariable Long conversationId,
                                               @RequestParam Long userId) {
        conversationService.leaveConversation(conversationId, userId);

        return ResponseEntity.ok("User with id " + userId +
                " has left the conversation " + conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long conversationId,
                                                @RequestParam Long requesterId) {
        conversationService.deleteConversation(conversationId, requesterId);
        return ResponseEntity.ok("Conversation deleted successfully");
    }
}
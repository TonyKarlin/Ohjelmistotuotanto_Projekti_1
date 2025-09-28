package backend_api.controller.messaging;


import backend_api.DTOs.conversations.ConversationDTO;
import backend_api.DTOs.conversations.ConversationParticipantResponse;
import backend_api.DTOs.conversations.ConversationRequest;
import backend_api.entities.Conversation;
import backend_api.entities.User;
import backend_api.enums.ParticipantRole;
import backend_api.services.ConversationService;
import backend_api.utils.customexceptions.InvalidConversationRequestException;
import backend_api.utils.customexceptions.UnauthorizedActionException;
import backend_api.utils.customexceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ConversationDTO> createConversation(
            @RequestBody ConversationRequest request,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        request.setCreatorId(authUser.getId());

        Conversation conversation = conversationService.createAConversation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ConversationDTO.fromConversationEntity(conversation));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversationByIdIfUserIsAParticipant(
            @PathVariable Long conversationId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        Conversation conversation = conversationService.getConversationById(conversationId);
        conversationService.validateUserIsParticipant(authUser, conversation);

        return ResponseEntity.ok(ConversationDTO.fromConversationEntity(conversation));
    }


    @GetMapping("/user/me")
    public ResponseEntity<List<ConversationDTO>> getMyConversations(Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        List<Conversation> conversations = conversationService.getConversationsByUserId(authUser.getId());

        List<ConversationDTO> dtos = conversations.stream()
                .map(ConversationDTO::fromConversationEntity)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{conversationId}/update")
    public ResponseEntity<?> updateConversation(@PathVariable Long conversationId,
                                                @RequestBody ConversationRequest request,
                                                Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        conversationService.validateOwnership(authUser, conversationId);

        Conversation updatedConversation = conversationService.updateConversation(conversationId, request);
        return ResponseEntity.ok(ConversationDTO.fromConversationEntity(updatedConversation));
    }


    @PutMapping("/{conversationId}/participants/{userId}")
    public ResponseEntity<ConversationParticipantResponse> addUserToConversation(@PathVariable Long conversationId,
                                                                                 @PathVariable Long userId,
                                                                                 Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        Conversation conversation = conversationService.getConversationById(conversationId);
        if (!conversation.isParticipant(authUser.getId())) {
            throw new UnauthorizedActionException("User not a participant of the conversation");
        }


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
            @PathVariable Long userId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        conversationService.validateOwnership(authUser, conversationId);

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
                                               Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        conversationService.leaveConversation(conversationId, authUser.getId());

        return ResponseEntity.ok("User: " + authUser.getUsername() +
                " has left the conversation (id): " + conversationId);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long conversationId,
                                                Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        conversationService.validateOwnership(authUser, conversationId);
        conversationService.deleteConversation(conversationId, authUser.getId());
        return ResponseEntity.ok("Conversation deleted successfully");
    }
}
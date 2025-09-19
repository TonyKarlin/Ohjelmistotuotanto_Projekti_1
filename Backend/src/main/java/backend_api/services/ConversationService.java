package backend_api.services;

import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.ConversationParticipant;
import backend_api.entities.ConversationParticipantId;
import backend_api.entities.User;
import backend_api.enums.ConversationType;
import backend_api.enums.ParticipantRole;
import backend_api.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserService userService;

    public ConversationService(ConversationRepository conversationRepository, UserService userService) {
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    // TODO: Implement conversation-related business logic
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Conversation not found with id: " + id));
    }

    // Validates Conversation Entity (DB)
    public void validateUserIsParticipant(User sender, Conversation conversation) {
        if (!conversation.hasParticipant(sender)) {
            throw new RuntimeException("User is not a participant of the conversation");
        }
    }

    public void validateParticipants(SendMessageRequest request) {
        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new RuntimeException("No participants specified for new conversation");
        }
    }

    // Checks request object to ensure sender is in participant list
    public void ensureSenderInParticipants(SendMessageRequest request, User sender) {
        if (!request.getParticipantIds().contains(sender.getId())) {
            request.getParticipantIds().add(request.getSenderId());
        }
    }

    private Conversation createConversationEntity(List<User> users, String type) {
        Conversation conversation = new Conversation();
        conversation.setType(type.equals("PRIVATE") ? ConversationType.PRIVATE : ConversationType.GROUP);

        // Reistaili aluksi, mutta pitäisi toimia ilman tätäkin. (Testaillaan lisää)
        // conversation = conversationRepository.save(conversation);

        for (User user : users) {
            ConversationParticipant participant = new ConversationParticipant(conversation, user, ParticipantRole.MEMBER);
            ConversationParticipantId participantId = new ConversationParticipantId(conversation.getId(), user.getId());
            participant.setId(participantId);
            conversation.getParticipants().add(participant);
        }

        return conversationRepository.save(conversation);
    }

    public Conversation createAConversation(SendMessageRequest request) {
        User sender = userService.getSender(request.getSenderId());
        // Ensures that the sender is part of the participants
        ensureSenderInParticipants(request, sender);

        List<User> users = userService.getConversationParticipants(request);

        if (users.size() != request.getParticipantIds().size()) {
            throw new RuntimeException("One or more users not found for the provided participant IDs");
        }

        // Check if it's a private conversation (exactly 2 participants)!!
        if (users.size() == 2) {
            Long senderId = request.getSenderId();
            Long receiverId = users.stream()
                    .map(User::getId)
                    .filter(id -> !id.equals(senderId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Receiver ID not found"));

            // Return existing private conversation if it exists
            return conversationRepository.findPrivateConversation(senderId, receiverId)
                    .orElseGet(() -> createConversationEntity(users, "PRIVATE"));
        }

        // Else create a new group conversation
        return createConversationEntity(users, "GROUP");
    }

}

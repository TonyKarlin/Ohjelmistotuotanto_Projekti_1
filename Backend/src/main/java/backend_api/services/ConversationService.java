package backend_api.services;

import backend_api.DTOs.ConversationRequest;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.ConversationParticipant;
import backend_api.entities.ConversationParticipantId;
import backend_api.entities.User;
import backend_api.enums.ConversationType;
import backend_api.enums.ParticipantRole;
import backend_api.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Conversation not found with id: " + id));
    }

    public List<Conversation> getConversationsByUserId(Long userId) {
        return conversationRepository.findByUserId(userId);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
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
    public void ensureSenderInParticipants(ConversationRequest request, User sender) {
        if (!request.getParticipantIds().contains(sender.getId())) {
            request.getParticipantIds().add(request.getCreatorId());
        }
    }

    private Conversation createConversationEntity(List<User> users, String type, String name, Long creatorId) {
        Conversation conversation = new Conversation(ConversationType.valueOf(type));
        conversation.setName(name);
        conversation.setCreatedBy(creatorId);

        for (User user : users) {
            ConversationParticipant participant = new ConversationParticipant(conversation, user, ParticipantRole.MEMBER);
            ConversationParticipantId participantId = new ConversationParticipantId(conversation.getId(), user.getId());
            participant.setId(participantId);
            conversation.getParticipants().add(participant);
        }

        return conversationRepository.save(conversation);
    }

    public Conversation createAConversation(ConversationRequest request) {
        User sender = userService.getSender(request.getCreatorId());
        // Ensures that the sender is part of the participants
        ensureSenderInParticipants(request, sender);

        List<User> users = userService.getConversationParticipants(request.getParticipantIds());
        String type = users.size() == 2 ? "PRIVATE" : "GROUP";

        if (users.size() != request.getParticipantIds().size()) {
            throw new RuntimeException("One or more users not found for the provided participant IDs");
        }

        // Check if it's a private conversation (exactly 2 participants)!!
        if (users.size() == 2) {
            Long senderId = request.getCreatorId();
            // Get the "other" user (the receiver)
            User receiver = users.stream()
                    .filter(user -> !user.getId().equals(senderId)) // keep only the "other" user
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("Receiver not found"));


            // Return existing private conversation if it exists
            return conversationRepository.findPrivateConversation(senderId, receiver.getId())
                    .orElseGet(() -> createConversationEntity(users, type, receiver.getUsername(), null));
        }

        // Else create a new group conversation
        String groupName = request.getName() != null && !request.getName().isEmpty() ? request.getName() : null;
        Long creatorId = request.getCreatorId() != null ? request.getCreatorId() : null;
        return createConversationEntity(users, type, groupName, type.equals("GROUP") ? creatorId : null);
    }

}

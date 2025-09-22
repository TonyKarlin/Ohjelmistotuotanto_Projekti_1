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
import backend_api.utils.customexceptions.*;
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

    public void createParticipant(Conversation conversation, User user, ParticipantRole role) {
        ConversationParticipant newParticipant = new ConversationParticipant(conversation, user, ParticipantRole.MEMBER);
        ConversationParticipantId participantId = new ConversationParticipantId(conversation.getId(), user.getId());
        newParticipant.setId(participantId);
        conversation.getParticipants().add(newParticipant);
    }

    public boolean isAlreadyParticipant(Conversation conversation, User user) {
        for (ConversationParticipant participant : conversation.getParticipants()) {
            if (participant.getUser().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
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

    public Conversation updateConversation(Long conversationId, ConversationRequest request) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() ->
                new ConversationNotFoundException("Conversation not found with id: " + conversationId));

        if (conversation.getType() != ConversationType.GROUP) {
            throw new PrivateConversationException("Cannot update a private conversation");
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            conversation.setName(request.getName());
        }
        return conversationRepository.save(conversation);
    }


    public void addUserToConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            throw new ConversationNotFoundException("Conversation not found with id: " + conversationId);
        }

        if (conversation.getType() != ConversationType.GROUP) {
            throw new PrivateConversationException("Cannot add user to a private conversation");
        }

        User user = userService.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        if (isAlreadyParticipant(conversation, user)) throw new UserAlreadyParticipantException(
                "User is already a participant in the conversation");

        createParticipant(conversation, user, ParticipantRole.MEMBER);

        conversationRepository.save(conversation);
    }

    public boolean removeUserFromConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            throw new ConversationNotFoundException("Conversation not found with id: " + conversationId);
        }

        if (conversation.getType() != ConversationType.GROUP) {
            throw new PrivateConversationException("Cannot remove user from a private conversation");
        }

        boolean removed = conversation.getParticipants().removeIf(participant -> participant.getUser().getId().equals(userId));
        if (removed) {
            conversationRepository.save(conversation);
        }
        return removed;
    }

    // Validates Conversation Entity (DB)
    public void validateUserIsParticipant(User sender, Conversation conversation) {
        if (!conversation.hasParticipant(sender)) {
            throw new InvalidConversationRequestException("User is not a participant in the conversation");
        }
    }

    public void validateParticipants(SendMessageRequest request) {
        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new InvalidConversationRequestException("Participant list cannot be empty");
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
            createParticipant(conversation, user, ParticipantRole.MEMBER);
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
            throw new UserNotFoundException("One or more users not found in the participant list");
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

    public void deleteConversation(Long conversationId, Long requesterId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with id: " + conversationId));

        if (conversation.getType() != ConversationType.GROUP) {
            throw new PrivateConversationException("Cannot delete a private conversation");
        }

        if (!conversation.getCreatedBy().equals(requesterId)) {
            throw new UnauthorizedActionException("Only the creator can delete the conversation");
        }

        conversationRepository.delete(conversation);
    }
}

package backend_api.services;

import backend_api.DTOs.conversations.ConversationRequest;
import backend_api.DTOs.messages.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.ConversationParticipant;
import backend_api.entities.ConversationParticipantId;
import backend_api.entities.User;
import backend_api.enums.ConversationType;
import backend_api.enums.ParticipantRole;
import backend_api.repository.ConversationRepository;
import backend_api.utils.customexceptions.*;
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

    public void createParticipant(Conversation conversation, User user, ParticipantRole role) {
        ConversationParticipant newParticipant = new ConversationParticipant(conversation, user, role);
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

        if (conversation.isPrivate()) {
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

        if (conversation.isPrivate()) {
            throw new PrivateConversationException("Cannot add user to a private conversation");
        }

        User user = userService.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        if (isAlreadyParticipant(conversation, user)) {
            throw new UserAlreadyParticipantException("User is already a participant in the conversation");
        }

        createParticipant(conversation, user, ParticipantRole.MEMBER);

        conversationRepository.save(conversation);
    }

    public boolean removeUserFromConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            throw new ConversationNotFoundException("Conversation not found with id: " + conversationId);
        }

        if (conversation.isPrivate()) {
            throw new PrivateConversationException("Cannot remove user from a private conversation");
        }

        boolean removed = conversation.getParticipants().removeIf(participant -> participant.getUser().getId().equals(userId));
        if (removed) {
            conversationRepository.save(conversation);
        }
        return removed;
    }

    public void leaveConversation(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) {
            throw new ConversationNotFoundException("Conversation not found with id: " + conversationId);
        }

        if (!conversation.isParticipant(userId)) {
            throw new InvalidConversationRequestException("User with id " + userId +
                    " is not a participant in the conversation " + conversationId + "\nUser must" +
                    " remove the contact instead");
        }

        if (conversation.isPrivate()) {
            throw new PrivateConversationException("Cannot leave a private conversation");
        }

        boolean removed = conversation.getParticipants().removeIf(participant ->
                participant.getUser().getId().equals(userId));

        if (removed) {
            conversationRepository.save(conversation);

        } else {
            throw new InvalidConversationRequestException("User with id " + userId +
                    " cannot leave the conversation " + conversationId);
        }
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


    private Conversation createPrivateConversation(User user, User contactUser) {
        // Creates a private conversation between two users with both as admins
        // Sets display name as the contact user's username for the user and vice versa
        Conversation conversation = new Conversation(ConversationType.PRIVATE);
        conversationRepository.saveAndFlush(conversation);

        createParticipant(conversation, user, ParticipantRole.ADMIN);
        createParticipant(conversation, contactUser, ParticipantRole.ADMIN);

        return conversation;
    }

    public Conversation createPrivateConversationForNewContacts(User user, User contactUser) {
        return conversationRepository.findPrivateConversation(user.getId(), contactUser.getId())
                .orElseGet(() -> {
                    Conversation conversation = createPrivateConversation(user, contactUser);
                    return conversationRepository.save(conversation);
                });
    }

    private Conversation createGroupConversationEntity(List<User> users, ConversationType type, String name, Long creatorId) {
        Conversation conversation = new Conversation(type);
        conversation.setName(name);
        conversation.setCreatedBy(creatorId);

        if (type != ConversationType.GROUP) {
            throw new InvalidConversationRequestException("Only group conversations can be created with this method");
        }

        for (User user : users) {
            ParticipantRole role = user.getId().equals(creatorId) ? ParticipantRole.OWNER : ParticipantRole.MEMBER;
            createParticipant(conversation, user, role);
        }


        return conversationRepository.save(conversation);
    }

    public Conversation createAConversation(ConversationRequest request) {
        User sender = userService.getSender(request.getCreatorId());
        // Ensures that the sender is part of the participants
        ensureSenderInParticipants(request, sender);

        List<User> users = userService.getConversationParticipants(request.getParticipantIds());

        if (users.size() != request.getParticipantIds().size()) {
            throw new UserNotFoundException("One or more users not found in the participant list");
        }

        ConversationType type = ConversationType.GROUP;
        String groupName = request.getName() != null && !request.getName().isEmpty() ? request.getName() : null;
        Long creatorId = request.getCreatorId() != null ? request.getCreatorId() : null;

        return createGroupConversationEntity(users, type, groupName, creatorId);
    }

    public void deleteConversation(Long conversationId, Long requesterId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with id: " + conversationId));

        if (conversation.isPrivate()) {
            throw new PrivateConversationException("Cannot delete a private conversation");
        }

        if (!conversation.isCreator(requesterId)) {
            throw new UnauthorizedActionException("Only the creator can delete the conversation");
        }

        conversationRepository.delete(conversation);
    }
}

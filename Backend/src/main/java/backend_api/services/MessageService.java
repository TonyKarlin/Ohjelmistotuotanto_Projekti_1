package backend_api.services;

import backend_api.DTOs.MessageContentDTO;
import backend_api.DTOs.MessageDTO;
import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.*;
import backend_api.enums.ConversationType;
import backend_api.repository.ConversationRepository;
import backend_api.repository.MessageRepository;
import backend_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    public MessageService(MessageRepository repository, UserRepository userRepository, ConversationRepository conversationRepository) {
        this.messageRepository = repository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
    }

    public User getSender(SendMessageRequest request) {
        return userRepository.findById(request.getSenderId()).orElseThrow(() ->
                new RuntimeException("User not found with id: " + request.getSenderId()));
    }


    public Message createMessage(SendMessageRequest request, Conversation conversation) {
        Message message = new Message();
        message.setSender(getSender(request));
        message.setConversation(conversation);
        message.setText(request.getText());

        // If there's an attachment, create a MessageContent entity and associate it with the message
        if (request.getFileData() != null) {
            MessageContent attachment = new MessageContent();
            attachment.setFileType(request.getFileType());
            attachment.setData(request.getFileData());
            attachment.setMessage(message);
            message.addAttachments(attachment);
        }
        return message;
    }


    public void isUserPartOfConversation(SendMessageRequest request, Conversation conversation) {
        User sender = getSender(request);

        if (!conversation.hasParticipant(sender)) {
            throw new RuntimeException("User is not a participant of the conversation");
        }
    }


    public Conversation createAConversation(SendMessageRequest request) {
        List<User> users = userRepository.findAllById(request.getParticipantIds());

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


    private Conversation createConversationEntity(List<User> users, String type) {
        Conversation conversation = new Conversation();
        conversation.setType(type.equals("PRIVATE") ? ConversationType.PRIVATE : ConversationType.GROUP);

        conversation = conversationRepository.save(conversation);

        for (User user : users) {
            ConversationParticipant participant = new ConversationParticipant(conversation, user, "MEMBER");
            ConversationParticipantId participantId = new ConversationParticipantId(conversation.getId(), user.getId());
            participant.setId(participantId);
            conversation.getParticipants().add(participant);
        }

        return conversationRepository.save(conversation);
    }

    public void validateParticipants(SendMessageRequest request) {
        if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
            throw new RuntimeException("No participants specified for new conversation");
        }
    }

    public void ensureSenderInParticipants(SendMessageRequest request) {
        if (!request.getParticipantIds().contains(request.getSenderId())) {
            request.getParticipantIds().add(request.getSenderId());
        }
    }


    public Message sendMessage(SendMessageRequest request) {
        // Check if a conversation exists. If not, create a new one.
        Conversation conversation;

        if (request.getConversationId() != null) {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));
        } else {
            validateParticipants(request);
            ensureSenderInParticipants(request);

            conversation = createAConversation(request);
        }
        return createAndSaveMessage(request, conversation);
    }

    private Message createAndSaveMessage(SendMessageRequest request, Conversation conversation) {
        // Validate that sender is part of the conversation
        isUserPartOfConversation(request, conversation);

        // Create message entity
        Message message = createMessage(request, conversation);

        // Save and return
        return messageRepository.save(message);
    }


    public List<Message> getMessagesByConversationId(Long conversationId) {
        return messageRepository.findMessagesByConversationId(conversationId);
    }


    public boolean deleteMessage(Long userId, Long messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isEmpty()) {
            return false;
        }

        Message message = messageOptional.get();

        // Check if the user is the sender (authorization)
        if (!message.getSender().getId().equals(userId)) {
            return false;
        }

        messageRepository.delete(message);
        return true;
    }
}

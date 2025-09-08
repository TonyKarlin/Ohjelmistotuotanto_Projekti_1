package backend_api.services;

import backend_api.DTOs.SendMessageRequest;
import backend_api.controller.MessageController;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.MessageContent;
import backend_api.entities.User;
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

    public Conversation createPrivateConversation(Long senderId, List<Long> participants) {
        Long receiverId = participants.stream()
                .filter(id -> !id.equals(senderId))
                .findFirst().orElseThrow(() -> new RuntimeException("Receiver ID not found"));

        return conversationRepository.findPrivateConversation(senderId, receiverId)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    User sender = userRepository.findById(senderId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + senderId));
                    User receiver = userRepository.findById(receiverId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + receiverId));
                    newConversation.addParticipant(sender);
                    newConversation.addParticipant(receiver);
                    return conversationRepository.save(newConversation);
                });
    }

    public Conversation createGroupConversation(List<Long> participantIds) {
        return conversationRepository.findGroupConversation(participantIds, participantIds.size())
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    List<User> participants = userRepository.findAllById(participantIds);
                    if (participants.size() != participantIds.size()) {
                        throw new RuntimeException("One or more users not found for the provided IDs");
                    }
                    participants.forEach(newConversation::addParticipant);
                    return conversationRepository.save(newConversation);
                });
    }

    public Conversation getOrCreateConversation(Long senderId, List<Long> participantIds) {
        if (!participantIds.contains(senderId)) participantIds.add(senderId);
        if (participantIds.size() == 2) {
            return createPrivateConversation(senderId, participantIds);
        } else {
            return createGroupConversation(participantIds);
        }
    }


    public Message sendMessage(SendMessageRequest request) {
        // Check if a conversation exists. If not, create a new one.
        Conversation conversation = getOrCreateConversation(request.getSenderId(), request.getParticipantIds());

        User sender = userRepository.findById(request.getSenderId()).orElseThrow(() ->
                new RuntimeException("User not found with id: " + request.getSenderId()));
        // Create the message entity
        Message message = new Message();
        message.setSender(sender);
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
        return messageRepository.save(message);
    }

    public boolean deleteMessage(Long userId, Long messageId) {
        try {
            Message message = messageRepository
                    .getMessageByIdAndSender_Id(messageId, userId)
                    .orElseThrow(() -> new RuntimeException("Message not found or not owned by user"));

            messageRepository.delete(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

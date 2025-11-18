package backend_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import backend_api.dto.messages.SendMessageRequest;
import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.MessageContent;
import backend_api.entities.User;
import backend_api.repository.MessageRepository;
import backend_api.utils.customexceptions.InvalidConversationRequestException;
import backend_api.utils.customexceptions.MessageNotFoundException;
import backend_api.utils.customexceptions.UnauthorizedActionException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final UserService userService;

    public MessageService(MessageRepository repository, ConversationService conversationService, UserService userService) {
        this.messageRepository = repository;
        this.userService = userService;
        this.conversationService = conversationService;

    }

    public Message setNewMessage(SendMessageRequest request, Conversation conversation) {
        Message message = new Message();
        message.setSender(userService.getUserOrThrow(request.getSenderId()));
        message.setConversation(conversation);
        message.setText(request.getText());
        return message;
    }

    public Message addAttachmentToMessage(SendMessageRequest request, Message message) {
        MessageContent attachment = new MessageContent();
        attachment.setFileType(request.getFileType());
        attachment.setData(request.getFileData());
        attachment.setMessage(message);
        message.addAttachments(attachment);
        return message;
    }

    public Message createMessage(SendMessageRequest request, Conversation conversation) {
        Message message = setNewMessage(request, conversation);

        // If there's an attachment, create a MessageContent entity and associate it with the message
        if (request.isFileDataValid()) {
            return addAttachmentToMessage(request, message);
        }
        return message;
    }

    public Message sendMessage(SendMessageRequest request, User sender) {
        if (!request.conversationExists()) {
            throw new InvalidConversationRequestException("Conversation ID is required to send a message");
        }

        Conversation conversation = conversationService.getConversationById(request.getConversationId());
        if (!conversation.isParticipant(sender.getId())) {
            throw new UnauthorizedActionException("User not a participant of this conversation");
        }
        request.setSenderId(sender.getId());
        return createAndSaveMessage(request, conversation);
    }

    private Message createAndSaveMessage(SendMessageRequest request, Conversation conversation) {
        // Validate that sender is part of the conversation
        User sender = userService.getUserOrThrow(request.getSenderId());
        conversationService.validateUserIsParticipant(sender, conversation);

        // Create message entity
        Message message = createMessage(request, conversation);

        // Save and return
        return messageRepository.save(message);
    }

    public Message editMessage(Long conversationId, Long messageId, Long userId, String newText) {
        Message message = messageRepository.findById(messageId).orElseThrow(()
                -> new MessageNotFoundException("Message not found with id: " + messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to edit this message");
        }

        if (!message.getConversation().getId().equals(conversationId)) {
            throw new InvalidConversationRequestException("Message does not belong to the given conversation");
        }

        message.setText(newText);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByConversationId(Long conversationId, User user) {
        // Varmistetaan että id on olemassa ja käytetään sitä
        Conversation conversation = conversationService.getConversationById(conversationId);
        // Vain osallistujat voivat hakea viestejä, joten tarkistetaan se
        if (!conversation.isParticipant(user.getId())) {
            throw new UnauthorizedActionException("User not a participant of this conversation");
        }

        Long id = conversation.getId();

        return messageRepository.findMessagesByConversationId(id);
    }

    public Optional<Message> getMessageByIdAndConversationId(Long messageId, Long conversationId, User user) {
        Conversation conversation = conversationService.getConversationById(conversationId);
        if (!conversation.isParticipant(user.getId())) {
            throw new UnauthorizedActionException("User not a participant of this conversation");
        }
        return messageRepository.findByIdAndConversationId(messageId, conversationId);
    }

    public void deleteMessage(Long userId, Long messageId, Long conversationId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));

        if (!message.getSender().getId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to delete this message");
        }

        if (!message.getConversation().getId().equals(conversationId)) {
            throw new InvalidConversationRequestException("Message does not belong to the given conversation");
        }

        messageRepository.delete(message);
    }

}

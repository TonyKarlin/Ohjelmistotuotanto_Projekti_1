package backend_api.services;

import backend_api.DTOs.SendMessageRequest;
import backend_api.entities.*;
import backend_api.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    public Message createMessage(SendMessageRequest request, Conversation conversation) {
        Message message = new Message();
        message.setSender(userService.getSender(request.getSenderId()));
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


    public Message sendMessage(SendMessageRequest request) {
        // Check if a conversation exists. If not, create a new one.
        Conversation conversation = (request.conversationExists()) ?
                conversationService.getConversationById(request.getConversationId()) :
                conversationService.createAConversation(request);

        return createAndSaveMessage(request, conversation);
    }

    private Message createAndSaveMessage(SendMessageRequest request, Conversation conversation) {
        // Validate that sender is part of the conversation
        User sender = userService.getSender(request.getSenderId());
        conversationService.validateUserIsParticipant(sender, conversation);

        // Create message entity
        Message message = createMessage(request, conversation);

        // Save and return
        return messageRepository.save(message);
    }


    public List<Message> getMessagesByConversationId(Long conversationId) {
        // Varmistetaan että id on olemassa ja käytetään sitä (Throws RuntimeException jos ei löydy)
        Conversation conversation = conversationService.getConversationById(conversationId);
        Long id = conversation.getId();

        return messageRepository.findMessagesByConversationId(id);
    }

    public Optional<Message> getMessageByIdAndConversationId(Long messageId, Long conversationId) {
        return messageRepository.findByIdAndConversationId(messageId, conversationId);
    }


    public boolean deleteMessage(Long userId, Long messageId, Long conversationId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isEmpty()) {
            return false;
        }

        Message message = messageOptional.get();

        // Katsotaan onko käyttäjä viestin lähettäjä
        if (!message.getSender().getId().equals(userId)) return false;
        // Katsotaan kuuluuko viesti oikeaan keskusteluun
        if (!message.getConversation().getId().equals(conversationId)) return false;

        messageRepository.delete(message);
        return true;
    }
}

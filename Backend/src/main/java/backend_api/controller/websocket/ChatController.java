package backend_api.controller.websocket;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import backend_api.dto.messages.DeleteMessageDTO;
import backend_api.dto.messages.EditMessageDTO;
import backend_api.dto.messages.MessageDTO;
import backend_api.dto.messages.SendMessageRequest;
import backend_api.entities.Message;
import backend_api.entities.User;
import backend_api.services.MessageService;
import backend_api.services.UserService;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(SendMessageRequest request, Principal principal) {
        Optional<User> sender = userService.getUserByUsername(principal.getName());
        if (sender.isEmpty()) {
            throw new IllegalArgumentException("Invalid sender");
        }

        Message message = messageService.sendMessage(request, sender.get());
        MessageDTO dto = MessageDTO.fromMessageEntity(message);

        messagingTemplate.convertAndSend("/topic/conversations/" + request.getConversationId(), dto);
    }

    @MessageMapping("/chat.editMessage")
    public void editMessage(EditMessageDTO request) {

        Message message = messageService.editMessage(
                request.getConversationId(),
                request.getMessageId(),
                request.getSenderId(),
                request.getText()
        );

        MessageDTO dto = MessageDTO.fromMessageEntity(message);

        messagingTemplate.convertAndSend("/topic/conversations/" + request.getConversationId(), dto);
    }

    @MessageMapping("/chat.deleteMessage")
    public void deleteMessage(DeleteMessageDTO request) {
        messageService.deleteMessage(
                request.getUserId(),
                request.getMessageId(),
                request.getConversationId()
        );

        messagingTemplate.convertAndSend("/topic/conversations/" + request.getConversationId(), request);
    }
}

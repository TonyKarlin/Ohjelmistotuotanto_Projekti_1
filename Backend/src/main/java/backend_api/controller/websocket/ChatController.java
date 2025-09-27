package backend_api.controller.websocket;

import backend_api.DTOs.messages.*;
import backend_api.entities.Message;
import backend_api.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(SendMessageRequest request) {
        Message message = messageService.sendMessage(request);
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

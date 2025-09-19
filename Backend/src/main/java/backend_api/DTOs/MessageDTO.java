package backend_api.DTOs;

import backend_api.entities.Message;

import java.time.LocalDateTime;
import java.util.List;

// MessageDTO for transferring message data including sender info and attachments
// to avoid exposing the entire Message entity and recursive relationships.
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderUsername;
    private String text;
    private LocalDateTime createdAt;
    private List<MessageContentDTO> attachments;

    public MessageDTO() {
    }

    public static MessageDTO fromMessageEntity(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setConversationId(message.getConversation().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setText(message.getText());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setAttachments(message.getContent().stream()
                .map(MessageContentDTO::fromMessageContentEntity)
                .toList());
        return dto;
    }

    // getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<MessageContentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MessageContentDTO> attachments) {
        this.attachments = attachments;
    }
}

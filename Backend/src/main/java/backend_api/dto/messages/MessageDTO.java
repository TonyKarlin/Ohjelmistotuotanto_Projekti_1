package backend_api.dto.messages;

import java.time.LocalDateTime;
import java.util.List;

import backend_api.entities.Message;

// MessageDTO for transferring message data including sender info and attachments
// to avoid exposing the entire Message entity and recursive relationships.
public class MessageDTO {

    private Long id;
    private Long senderId;
    private String senderUsername;
    private String text;
    private LocalDateTime createdAt;
    private String senderProfilePicture;
    private List<MessageContentDTO> attachments;


    public static MessageDTO fromMessageEntity(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getUsername());
        dto.setText(message.getText());
        dto.setCreatedAt(message.getCreatedAt());

        String profilePicture = message.getSender().getProfilePicture();
        if (profilePicture == null || profilePicture.isEmpty()) {
            profilePicture = "http://localhost:8081/uploads/default.png";
        } else if (!profilePicture.startsWith("http")) {
            profilePicture = "http://localhost:8081/uploads/" + profilePicture;
        }

        dto.setSenderProfilePicture(profilePicture);
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

    public void setSenderProfilePicture(String senderProfilePicture) {
        this.senderProfilePicture = senderProfilePicture;
    }

    public String getSenderProfilePicture() {
        return senderProfilePicture;
    }

    public List<MessageContentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MessageContentDTO> attachments) {
        this.attachments = attachments;
    }
}

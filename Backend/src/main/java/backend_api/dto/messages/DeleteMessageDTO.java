package backend_api.dto.messages;

public class DeleteMessageDTO {

    private Long messageId;
    private Long conversationId;
    private Long userId;

    public DeleteMessageDTO(Long messageId, Long conversationId) {
        this.messageId = messageId;
        this.conversationId = conversationId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

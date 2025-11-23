package backend_api.dto.messages;

public class EditMessageDTO {

    private Long messageId;
    private Long conversationId;
    private Long senderId;
    private String text;

    public EditMessageDTO() {
    }

    public EditMessageDTO(Long messageId, Long conversationId, Long senderId, String text) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.text = text;
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

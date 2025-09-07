package backend_api.DTOs;


// DTO(Data Transfer Object) for sending a message with optional file attachment
// Example usage: sendMessage(new SendMessageRequest(senderId, conversationId, "Hello World", "image/png", fileData));
public class SendMessageRequest {
    private Long senderId;
    private Long receiverId;
    private String text;
    private String fileType; // Example: "image/png", "application/pdf"
    private byte[] fileData; // Binary data for the message attachments (photos, videos, documents)

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String content) {
        this.text = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}

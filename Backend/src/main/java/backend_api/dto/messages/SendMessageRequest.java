package backend_api.dto.messages;

import java.util.List;

public class SendMessageRequest {

    private Long senderId;
    private Long conversationId;
    private List<Long> participantIds;
    private String text;
    private String fileType; // Example: "image/png", "application/pdf"
    private byte[] fileData; // Binary data for the message attachments (photos, videos, documents)


    public boolean conversationExists() {
        return conversationId != null;
    }

    public boolean hasAttachment() {
        return fileData != null && fileType != null && !fileType.isEmpty();
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isFileDataValid() {
        return fileData != null && fileData.length > 0;
    }
}

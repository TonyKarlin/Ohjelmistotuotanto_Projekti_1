package backend_api.DTOs;

import backend_api.entities.MessageContent;

// DTO for transferring message content metadata (like file type and ID)
// Used within MessageDTO to represent attachments without exposing full entity
public class MessageContentDTO {
    private Long id;
    private String fileType;

    public MessageContentDTO() {
    }

    public static MessageContentDTO fromMessageContentEntity(MessageContent content) {
        MessageContentDTO dto = new MessageContentDTO();
        dto.setId(content.getId());
        dto.setFileType(content.getFileType());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}

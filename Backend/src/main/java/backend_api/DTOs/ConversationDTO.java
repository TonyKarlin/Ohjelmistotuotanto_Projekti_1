package backend_api.DTOs;

import backend_api.entities.Conversation;

import java.time.LocalDateTime;

public class ConversationDTO {
    private Long id;
    private String type;
    private String name;
    private Long createdBy;
    private LocalDateTime createdAt;

    public ConversationDTO() {
    }

    public static ConversationDTO fromConversationEntity(Conversation conversation) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setCreatedBy(conversation.getCreatedBy());
        dto.setType(conversation.getType().name());
        dto.setName(conversation.getName());
        dto.setCreatedAt(conversation.getCreatedAt());
        return dto;
    }

    // getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

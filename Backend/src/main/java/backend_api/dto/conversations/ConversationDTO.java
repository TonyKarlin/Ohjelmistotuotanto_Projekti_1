package backend_api.dto.conversations;

import java.time.LocalDateTime;
import java.util.List;

import backend_api.entities.Conversation;

public class ConversationDTO {

    private Long id;
    private String type;
    private String name;
    private Long createdBy;
    private LocalDateTime createdAt;
    private List<ParticipantDTO> participants;

    public static ConversationDTO fromConversationEntity(Conversation conversation) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setCreatedBy(conversation.getCreatedBy());
        dto.setType(conversation.getType().name());
        dto.setName(conversation.getName());
        dto.setCreatedAt(conversation.getCreatedAt());
        dto.setParticipants(conversation.getParticipants()
                .stream()
                .map(ParticipantDTO::fromParticipantEntity)
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

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }
}

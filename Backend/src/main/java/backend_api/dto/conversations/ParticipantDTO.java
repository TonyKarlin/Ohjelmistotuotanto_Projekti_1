package backend_api.dto.conversations;

import backend_api.entities.ConversationParticipant;

public class ParticipantDTO {

    private Long userId;
    private String username;
    private String role;

    public static ParticipantDTO fromParticipantEntity(ConversationParticipant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setUserId(participant.getUser().getId());
        dto.setUsername(participant.getUser().getUsername());
        dto.setRole(participant.getRole().name());
        return dto;
    }

    public ParticipantDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

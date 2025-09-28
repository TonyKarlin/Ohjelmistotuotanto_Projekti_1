package model;

import lombok.Data;

@Data
public class ConversationParticipant {

    private String username;
    private String role;
    private int userId;

    public ConversationParticipant() {}

    // Constructor for mapping a conversation participant
    public ConversationParticipant(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}

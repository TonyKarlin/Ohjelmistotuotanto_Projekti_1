package backend_api.entities;

import backend_api.enums.ParticipantRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "conversation_participants")
public class ConversationParticipant {
    @EmbeddedId
    private ConversationParticipantId id;

    @ManyToOne
    @MapsId("conversationId")
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated
    private ParticipantRole role = ParticipantRole.MEMBER;

    // Used in Private conversations to display the other user's name.
    private String displayName; // optional, can be null and probably will be null in group conversations.
    private LocalDateTime joinedAt = LocalDateTime.now();

    public ConversationParticipant() {
    }

    public ConversationParticipant(Conversation newConversation, User user, ParticipantRole role) {
        this.conversation = newConversation;
        this.user = user;
        this.role = role;
    }


    // getterit ja setterit
    public ConversationParticipantId getId() {
        return id;
    }

    public void setId(ConversationParticipantId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ParticipantRole getRole() {
        return role;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public boolean isParticipant(User user) {
        return this.user.equals(user);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}

package backend_api.entities;

import backend_api.enums.ConversationType;
import backend_api.enums.ParticipantRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    // Comes from the request or is null for private chats. Private chats name will be generated on the frontend.
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConversationParticipant> participants = new ArrayList<>();

    @Column
    private Long createdBy;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    public Conversation() {
    }

    public Conversation(ConversationType type) {
        this.type = type;
    }

    @PrePersist
    @PreUpdate
    private void validateConversation() {
        if (!hasSingleOwner()) {
            throw new IllegalStateException("Conversation must have one or fewer owners.");
        }
        if (isPrivate() && this.participants.size() > 2) {
            throw new IllegalStateException("Private conversations must have exactly two participants.");
        }
    }

    public void addParticipant(User user, ParticipantRole role) {
        ConversationParticipant participant = new ConversationParticipant(this, user, role);
        if (!participants.contains(participant)) participants.add(participant);
    }


    public void removeParticipant(ConversationParticipant participant) {
        participants.remove(participant);
        participant.setConversation(null);
    }

    public boolean hasParticipant(User user) {
        return participants.stream().anyMatch(p -> p.isParticipant(user));
    }

    public ParticipantRole getParticipantRole(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return participants.stream()
                .filter(p -> p.getUser() != null && p.getUser().getId().equals(user.getId()))
                .map(ConversationParticipant::getRole)
                .findFirst()
                .orElse(null);
    }


    // getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConversationType getType() {
        return type;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public List<ConversationParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ConversationParticipant> participants) {
        this.participants = participants;
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

    public boolean isPrivate() {
        return this.type == ConversationType.PRIVATE;
    }

    public boolean isAGroup() {
        return this.type == ConversationType.GROUP;
    }

    public boolean isParticipant(Long userId) {
        return participants.stream().anyMatch(p ->
                p.getUser().getId().equals(userId));
    }

    public boolean isCreator(Long userId) {
        return this.createdBy.equals(userId);
    }

    public boolean isCreatorParticipant(Long userId) {
        return participants.stream().anyMatch(p ->
                p.getUser().getId().equals(userId) && p.getRole() == ParticipantRole.OWNER);
    }

    public boolean hasSingleOwner() {
        return participants.stream()
                .filter(p -> p.getRole() == ParticipantRole.OWNER)
                .count() <= 1;
    }

}

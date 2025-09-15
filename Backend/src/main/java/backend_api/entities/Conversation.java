package backend_api.entities;

import backend_api.enums.ConversationType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConversationType type;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConversationParticipant> participants = new ArrayList<>();

    public Conversation() {
    }

    public Conversation(ConversationType type) {
        this.type = type;
    }

    public void addParticipant(User user, String role) {
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


    // getterit ja setterit
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

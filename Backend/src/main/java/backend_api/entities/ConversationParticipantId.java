package backend_api.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConversationParticipantId implements Serializable {
    private Long conversationId;
    private Long userId;


    public ConversationParticipantId() {
    }

    public ConversationParticipantId(Long id, Long id1) {
        this.conversationId = id;
        this.userId = id1;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    // Overriding equals and hashCode is crucial for composite keys to function correctly with JPA.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationParticipantId that)) return false;
        return conversationId.equals(that.conversationId) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, userId);
    }
}

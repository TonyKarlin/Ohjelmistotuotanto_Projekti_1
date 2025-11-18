package backend_api.dto.conversations;

import java.util.List;

public class ConversationRequest {

    private Long creatorId;
    private List<Long> participantIds;
    private String name;   // Custom for Groups. Set to Receiver Username for Private

    public ConversationRequest() {
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

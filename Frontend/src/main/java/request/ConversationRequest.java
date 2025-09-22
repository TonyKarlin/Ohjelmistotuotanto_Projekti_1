package request;

import lombok.Data;

import java.util.List;

@Data
public class ConversationRequest {

    private int creatorId;
    private String name;
    List<Integer> participantsIds;


    public ConversationRequest() {}

    public ConversationRequest(int creatorId, String name,List<Integer> participantIds) {
        this.creatorId = creatorId;
        this.name = name;
        this.participantsIds = participantIds;
    }
}

package request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConversationRequest {

    private int creatorId;
    private String name;
    private List<Integer> participantIds = new ArrayList<>();


    public ConversationRequest() {}

    public ConversationRequest(int creatorId, String name,List<Integer> participantIds) {
        this.creatorId = creatorId;
        this.name = name;
        this.participantIds = participantIds;
    }
}

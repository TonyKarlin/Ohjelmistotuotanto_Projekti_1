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

    //Construct for creating a conversation
    public ConversationRequest(int creatorId, String name,List<Integer> participantIds) {
        this.creatorId = creatorId;
        this.name = name;
        this.participantIds = participantIds;
    }

    //Construct for changing name in conversation
    public ConversationRequest(String name) {
        this.name = name;
    }
}

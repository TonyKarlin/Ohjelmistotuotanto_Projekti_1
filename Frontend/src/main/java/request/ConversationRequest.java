package request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConversationRequest {

    private int creatorId;
    private String name;
    private List<Integer> participantIds = new ArrayList<>();
    private int conversationId;
    private String token;
    private int participantId;


    public ConversationRequest() {}

    //Construct for creating a conversation
    public ConversationRequest(int creatorId, String name,List<Integer> participantIds) {
        this.creatorId = creatorId;
        this.name = name;
        this.participantIds = participantIds;
    }

    //Construct for changing name in conversation
    public ConversationRequest(String name, int conversationId, String token) {
        this.name = name;
        this.conversationId = conversationId;
        this.token = token;
    }
}

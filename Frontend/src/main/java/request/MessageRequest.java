package request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class MessageRequest {

    private  String text;
    private List<Integer> participantIds;


    public MessageRequest() {}

    public MessageRequest(List<Integer> participantIds, String text) {
        this.participantIds = participantIds;
        this.text = text;
    }
}

package request;

import lombok.Data;
import model.User;

import java.util.ArrayList;
import java.util.List;


@Data
public class MessageRequest {

    private String text;
    private List<Integer> participantIds;;
    private int senderId;


    public MessageRequest() {}

    public MessageRequest(List<Integer> participantIds, String text, Integer senderId) {
        this.participantIds = participantIds;
        this.text = text;
        this.senderId = senderId;

    }

}

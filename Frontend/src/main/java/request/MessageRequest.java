package request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class MessageRequest {

    private  String text;
    private List<Integer> participantsId;


    public MessageRequest() {}

    public MessageRequest(List<Integer> participantsId, String text) {
        this.participantsId = participantsId;
        this.text = text;
    }
}

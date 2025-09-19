package request;

import lombok.Data;

@Data
public class MessageRequest {

    private String text;
    private int senderId;


    public MessageRequest() {}

    public MessageRequest(String text, Integer senderId) {
        this.text = text;
        this.senderId = senderId;

    }

}

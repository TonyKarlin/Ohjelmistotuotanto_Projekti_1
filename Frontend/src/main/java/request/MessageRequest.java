package request;

import lombok.Data;
import model.User;

@Data
public class MessageRequest {

    private String text;
    private int userId;
    private int senderId;
    private int conversationId;
    private int messageId;
    private String token;


    public MessageRequest() {}

    public MessageRequest(String text, int conversationId, String token) {
        this.text = text;
        this.token = token;
        this.conversationId = conversationId;
    }
}

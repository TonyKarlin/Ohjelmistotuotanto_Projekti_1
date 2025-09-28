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

    //Construct to send message to conversation
    public MessageRequest(String text, int conversationId, String token) {
        this.text = text;
        this.token = token;
        this.conversationId = conversationId;
    }

    //Construct to modify message in a conversation
    public MessageRequest(int conversationId, String text, int messageId, String token) {
        this.conversationId = conversationId;
        this.text = text;
        this.messageId = messageId;
        this.token = token;
    }

    //Construct to delete message in conversation
    public MessageRequest(int conversationId, int messageId, String token) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.token = token;
    }

}

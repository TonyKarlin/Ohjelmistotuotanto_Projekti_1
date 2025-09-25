package request;

import lombok.Data;

@Data
public class MessageRequest {

    private String text;
    private int senderId;
    private int conversationId;
    private int messageId;


    public MessageRequest() {}


    //Construct to send message to conversation
    public MessageRequest(String text, int senderId, int conversationId) {
        this.text = text;
        this.senderId = senderId;
        this.conversationId = conversationId;

    }
    //Construct to modify message in a conversation
    public MessageRequest(int conversationId, String text, int messageId) {
        this.conversationId = conversationId;
        this.text = text;
        this.messageId = messageId;
    }

}

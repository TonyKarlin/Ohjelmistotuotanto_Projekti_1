package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// Message object
// Turns Json Data to Message Object

import java.util.List;

@Data
public class Message {

    private int id;
    private int conversationId;
    private int senderId;
    private String senderUsername;
    private String text;
    private String createdAt;

    @JsonProperty("attachments")
    private List<MessageAttachment> messageAttachments;

    public Message() {}


}

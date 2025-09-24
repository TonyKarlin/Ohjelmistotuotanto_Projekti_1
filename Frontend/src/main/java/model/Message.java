package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.List;

// Message object
// Turns Json Data to Message Object
//Using lombok dependency that creates automatically setters and getters
@Data
public class Message {

    private int id;
    private int senderId;
    private String senderUsername;
    private String text;
    private String createdAt;

    @JsonProperty("attachments")
    private List<MessageAttachment> messageAttachments;

    //Empty construct for Object mapper
    public Message() {}


}

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
    private String senderProfilePicture;

    @JsonProperty("attachments")
    private List<MessageAttachment> messageAttachments;


    public Message() {
        //Empty construct for Object mapper
    }


}

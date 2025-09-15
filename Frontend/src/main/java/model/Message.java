package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// Message object
// Turns Json Data to Message Object

import java.util.List;

@Data
public class Message {

    private int id;
    private User sender;
    private Conversation conversation;
    private String text;
    private String createdAt;

    @JsonProperty("content")
    private List<MessageContent> messageContent;

    public Message() {}


}

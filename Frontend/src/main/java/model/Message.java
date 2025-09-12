package model;

import lombok.Data;

@Data
public class Message {

    private int id;
    private User sender;
    private Conversation conversation;
    private String text;
    private String created_at;
    private int conversation_id;

    public Message() {}


}

package model;

import java.util.List;

import lombok.Data;

//Conversation model to save server responses to Conversation object
//Using lombok dependency that creates automatically setters and getters
@Data
public class Conversation {

    private int id;
    private String type;
    private String name;
    private int createdBy;
    private String createdAt;
    private List<User> participants;

    //Empty construct for Object mapper
    public Conversation() {
    }

}

package model;

import lombok.Data;

//Using lombok dependency that creates automatically setters and getters
@Data
public class MessageAttachment {

    private int id;
    private String fileType;
    private int messageId;

    //Empty construct for Object mapper
    public MessageAttachment() {}
}

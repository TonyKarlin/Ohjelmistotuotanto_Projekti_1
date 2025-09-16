package model;

import lombok.Data;

@Data
public class MessageAttachment {

    private int id;
    private String fileType;
    private int messageId;

    public MessageAttachment() {}
}

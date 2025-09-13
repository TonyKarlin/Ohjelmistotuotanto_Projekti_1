package model;

import lombok.Data;

@Data
public class MessageContent {

    private int id;
    private String fileType;
    private int messageId;

    public MessageContent() {}
}

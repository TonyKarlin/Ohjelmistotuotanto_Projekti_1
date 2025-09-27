package model;

import lombok.Data;

@Data
public class ContactResponse {

    private Contact contact;
    private Conversation conversation;

    // Empty constructor for Object mapper
    public ContactResponse() {
    }
}

package model;

import lombok.Data;

@Data
public class ContactResponse {

    private Contact contact;
    private Conversation conversation;


    public ContactResponse() {
        // Empty constructor for Object mapper
    }
}

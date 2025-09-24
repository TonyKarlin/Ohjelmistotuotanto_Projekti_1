package model;

import lombok.Data;

@Data
public class Contact {

    private int contactId;
    private int contactUserId;
    private String contactUsername;
    private String status;

    //Empty construct for Object mapper
    public Contact() {

    }

}

package model;

import lombok.Data;

@Data
public class Contact {

    private int id;
    private int contactUserId;
    private String contactUsername;
    private String status;
    private int initiatorId; // Who initiated the friend request


    public Contact() {
        //Empty construct for Object mapper
    }

}

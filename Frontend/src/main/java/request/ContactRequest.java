package request;

import lombok.Data;

@Data
public class ContactRequest {

    private int userId;
    private int contactUserId;
    private String token;

    public ContactRequest() {
    }

    public ContactRequest(int userId, int contactUserId, String token) {
        this.userId = userId;
        this.contactUserId = contactUserId;
        this.token = token;
    }
}

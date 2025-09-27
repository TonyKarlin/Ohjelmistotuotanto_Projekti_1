package request;

public class ContactRequest {

    private int userId;
    private int contactUserId;

    public ContactRequest() {
    }

    public ContactRequest(int userId, int contactUserId) {
        this.userId = userId;
        this.contactUserId = contactUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(int contactUserId) {
        this.contactUserId = contactUserId;
    }
}

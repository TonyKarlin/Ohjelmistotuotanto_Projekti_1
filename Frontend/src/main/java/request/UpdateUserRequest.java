package request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private String email;
    private int userId;
    private String oldPassword;
    private String newPassword;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String username, String email, int userId, String oldPassword, String newPassword) {
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.oldPassword = oldPassword;
        this. newPassword = newPassword;
    }
}

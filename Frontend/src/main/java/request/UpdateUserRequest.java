package request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String username;
    private String email;
    private String password;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

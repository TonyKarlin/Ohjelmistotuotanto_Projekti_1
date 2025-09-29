package request;

import lombok.Data;

import java.io.File;

@Data
public class UpdateUserRequest {

    private String username;
    private String email;
    private String password;
    private File profilePicture;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

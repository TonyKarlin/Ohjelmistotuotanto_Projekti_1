package request;

import lombok.Data;

// lombok dependency so no need to write setters and getters
@Data
public class LoginRequest {
    private String username;
    private String password;


    //Empty construct for Object mapper
    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }
}

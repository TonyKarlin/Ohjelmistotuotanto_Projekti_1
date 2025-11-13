package request;

import lombok.Data;

// lombok dependency so no need to write setters and getters
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String language;


    //Empty construct for Object mapper
    public LoginRequest() {}

    public LoginRequest(String username, String password, String language) {
        this.username = username;
        this.password = password;
        this.language = language;

    }
}

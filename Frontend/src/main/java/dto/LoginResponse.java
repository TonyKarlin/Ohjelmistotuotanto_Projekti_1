package dto;

import lombok.Data;
import request.LoginRequest;

@Data
public class LoginResponse {
    private String token;
    private String message;

    public LoginResponse() {
    }


    public LoginResponse(String token, String message) {
        this.message = message;
        this.token = token;
    }
}

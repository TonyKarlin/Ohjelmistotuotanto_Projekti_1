package backend_api.DTOs;

public class LoginResponse {

    private final String message;
    private String token; // voidaan lisätä kun JWT käytössä

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}

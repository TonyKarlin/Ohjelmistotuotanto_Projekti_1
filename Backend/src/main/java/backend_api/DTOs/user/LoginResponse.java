package backend_api.DTOs.user;

public class LoginResponse {

    private final String message;
    private String token; // voidaan lisätä kun JWT käytössä
    private Long id;
    private  String username;
    private String email;

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String message, String token,long id, String username, String email ) {
        this.message = message;
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}

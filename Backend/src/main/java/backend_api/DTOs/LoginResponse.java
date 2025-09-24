package backend_api.DTOs;

public class LoginResponse {

    private final String message;
    private String token;
    private Long id;
    private  String username;
    private String email;
    private String profilePictureUrl;

    public LoginResponse(String message) {
        this.message = message;
    }

    public LoginResponse(String message, String token,long id, String username, String email, String profilePictureUrl) {
        this.message = message;
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}

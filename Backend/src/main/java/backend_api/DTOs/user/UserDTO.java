package backend_api.DTOs.user;

import backend_api.entities.User;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String email;
    private final String profilePictureUrl;
    private final String language;

    public UserDTO(User user, String language) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profilePictureUrl = user.getProfilePicture() != null
                ? "http://localhost:8081/uploads/" + user.getProfilePicture()
                : "http://localhost:8081/uploads/default.png";
        this.language = user.getLanguage();
    }

    public UserDTO(User user) {
        this(user, user.getLanguage());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getLanguage() { return language; }
}

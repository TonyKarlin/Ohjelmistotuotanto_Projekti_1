package backend_api.DTOs;

import backend_api.entities.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String profilePictureUrl;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profilePictureUrl = user.getProfilePicture() != null
                ? "http://localhost:8080/" + user.getProfilePicture()
                : null;
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
}

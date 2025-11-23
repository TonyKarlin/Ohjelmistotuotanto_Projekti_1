package model;

import lombok.Data;

//When register or login creates User Object from the Json Data
//Using lombok dependency that creates automatically setters and getters
@Data
public class User {

    private String username;
    private String email;
    private String password;
    private int id;
    private String createdAt;
    private String token;
    private String role;
    private String profilePictureUrl;
    private String language;

    public User() {
        // Default constructor (needed for Jackson/ObjectMapper)

    }

    // Constructor for creating/registering a new user
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.language = "en";
    }

}

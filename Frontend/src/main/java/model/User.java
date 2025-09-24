package model;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int userId;

    // Default constructor (needed for Jackson/ObjectMapper)
    public User() {

    }

    // Constructor for creating/registering a new user
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Constructor for mapping a conversation participant
    public User(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

}

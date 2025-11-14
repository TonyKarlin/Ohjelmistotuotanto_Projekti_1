package model;

import lombok.Data;

/**
 * Represents the JSON response from loginUser or updateUser API calls.
 * Contains the authentication token and the corresponding User object.
 */
@Data
public class UserResponse {

    private String token;
    private User user;

    public UserResponse() {
        //Empty user constructor for Jackson object mapper
    }
}

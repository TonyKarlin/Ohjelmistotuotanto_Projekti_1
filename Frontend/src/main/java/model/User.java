package model;

import lombok.Data;

//When register or login creates User Object from the Json Data

@Data
public class User {
    private String username;
    private String email;
    private String password;
    private int id;
    private String createdAt;
    private String token;

    //Empty construct for Object mapper
    public User() {

    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}

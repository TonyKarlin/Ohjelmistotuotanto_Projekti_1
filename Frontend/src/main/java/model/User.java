package model;

import lombok.Data;


@Data
public class User {
    private String username;
    private String email;
    private String password;
    private int id;
    private String createdAt;

    //Empty construct for Object mapper
    public User() {

    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}

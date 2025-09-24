package model;

import lombok.Data;

@Data
public class Participant {

    private int userId;
    private String username;
    private String role;

    public Participant() {
    }
}

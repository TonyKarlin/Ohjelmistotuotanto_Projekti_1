package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class Conversation {

    private int id;
    private String type;
    private String name;
    private  int createdBy;
    private String createdAt;

    public Conversation() {}



}

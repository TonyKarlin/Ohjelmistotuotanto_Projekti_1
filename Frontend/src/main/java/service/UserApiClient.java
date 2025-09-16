package service;


import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.databind.JsonNode;
import request.LoginRequest;
import model.User;

public class UserApiClient implements ApiClient {

    String registerUrl = ("http://localhost:8080/api/users/register");
    String loginUrl = ("http://localhost:8080/api/users/login");
    String usersUrl = ("http://localhost:8080/api/users");

    public UserApiClient() throws MalformedURLException {

    }

    public User registerUser(User user) {
        try {
            ApiResponse response =  sendPostRequest(registerUrl, user);
            if (response.isSuccess()) {
                System.out.println(response.body);
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.out.println("Failed to register user. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public User loginUser(LoginRequest loginRequest) {
        try {
            ApiResponse response = sendPostRequest(loginUrl, loginRequest);
            if (response.isSuccess()) {
                return objectMapper.readValue(response.body, User.class);
            } else {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String errorMessage = jsonNode.has("message")
                        ? jsonNode.get("message").asText()
                        : response.getBody();
                System.out.println(errorMessage);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to login", e);
        }

    }

//    public void getUsers() {
//        try {
//            System.out.println(sendGetRequest(usersUrl));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


}

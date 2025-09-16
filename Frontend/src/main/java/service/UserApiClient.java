package service;


import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.databind.JsonNode;
import dto.LoginResponse;
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
            HttpResponse response = sendPostRequest(registerUrl, user);
            if (response.statusCode >= 200 && response.statusCode < 300) {
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.err.println("Failed to register user. Status: " + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public User loginUser(LoginRequest loginRequest) {
        try {
            HttpResponse response = sendPostRequest(loginUrl, loginRequest);
            if (response.statusCode >= 200 && response.statusCode < 300) {
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.err.println("Failed to login. Status: " + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to login", e);
        }
    }

    public void getUsers() {
        try {
            System.out.println(sendGetRequest(usersUrl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

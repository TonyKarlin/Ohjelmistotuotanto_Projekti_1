package service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.databind.JsonNode;

import model.User;
import request.LoginRequest;
import request.UpdateUserRequest;
import utils.ApiUrl;

public class UserApiClient implements ApiClient {

    String registerUrl = ApiUrl.getApiUrl() + "/users/register";
    String loginUrl = ApiUrl.getApiUrl() + "/users/login";
    String usersUrl = ApiUrl.getApiUrl() + "/users";

    public UserApiClient() throws MalformedURLException {

    }

    public User registerUser(User user) {
        try {
            ApiResponse response = sendPostRequest(registerUrl, user);
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

    public User updateUser(UpdateUserRequest request, User user) {
        try {
            String url = usersUrl + "/"+ request.getUserId();
            String token = user.getToken();
            ApiResponse response = sendPutRequestWithObjectAndToken(url, request, token);
            if (response.isSuccess()) {
                System.out.println(response.body);
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.out.println("Failed to Update user. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to Update user", e);
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

    public User getUserByUsername(String username) {
        try {
            ApiResponse response = sendGetRequest(usersUrl + "/username/" + username);
            if (response.isSuccess()) {
                System.out.println(response.body);
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.out.println("Failed to fetch a User. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to GET a user", e);
        }
    }

}

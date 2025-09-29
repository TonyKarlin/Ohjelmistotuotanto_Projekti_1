package service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import model.User;
import model.UserResponse;
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

    public UserResponse updateUser(UpdateUserRequest request, User user) {
        try {
            String url = usersUrl + "/" + user.getId();
            String token = user.getToken();
            ApiResponse response = sendPutRequestWithObjectAndToken(url, request, token);
            if (response.isSuccess()) {
                System.out.println(response.body);
                return objectMapper.readValue(response.body, UserResponse.class);
            } else {
                System.out.println("Failed to Update user. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to Update user", e);
        }
    }

    public User updateUserProfilePicture(File file, User user) {
        try {
            String url = usersUrl + "/" + user.getId() + "/profile-picture";
            String token = user.getToken();
            ApiResponse response = sendFile(url, file, token);
            if (response.isSuccess()) {
                System.out.println(response.body);
                return objectMapper.readValue(response.body, User.class);
            } else {
                System.out.println("Failed to Update user profile picture. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public UserResponse loginUser(LoginRequest loginRequest) {
        try {
            ApiResponse response = sendPostRequest(loginUrl, loginRequest);
            if (response.isSuccess()) {
                return objectMapper.readValue(response.body, UserResponse.class);
            } else {
                System.out.println("Failed to Login: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to login", e);
        }
    }

    public User getUserByUsername(String username, String token) {
        try {
            String url = usersUrl + "/username/" + username;
            ApiResponse response = sendGetRequest(url, token);
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

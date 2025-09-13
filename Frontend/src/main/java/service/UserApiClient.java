package service;


import java.io.IOException;
import java.net.MalformedURLException;
import request.LoginRequest;
import model.User;

public class UserApiClient implements ApiClient{

    String registerUrl = ("http://localhost:8080/api/users/register");
    String loginUrl = ("http://localhost:8080/api/users/login");
    String usersUrl = ("http://localhost:8080/api/users");

    public UserApiClient() throws MalformedURLException {

    }

    public User registerUser(User user) {
        try {
            String responseJson = sendPostRequest(registerUrl, user);
            return objectMapper.readValue(responseJson, User.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to register user", e);
        }

    }

    public User loginUser(LoginRequest loginRequest) {
        try {
            String responseJson = sendPostRequest(loginUrl, loginRequest);
            return objectMapper.readValue(responseJson, User.class);
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

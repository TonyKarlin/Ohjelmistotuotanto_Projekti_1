package service;

import model.User;
import request.MessageRequest;
import java.io.IOException;

public class MessageApiClient implements ApiClient {

    String baseUrl = ("http://localhost:8080/api/users");
    User user;

    public MessageApiClient() {
    }

    public String sendMessage(User user, MessageRequest request) throws IOException {
        String url = baseUrl + "/" + user.getId() + "/messages";
        return sendPostRequest(url, request);
    }


}

package service;

import model.Message;
import model.User;
import request.MessageRequest;

import java.io.IOException;

public class MessageApiClient implements ApiClient {

    String messageUrl = ("http://localhost:8080/api/conversations/messages");
    User user;

    public MessageApiClient() {
    }

    public Message sendMessage(MessageRequest request) {
        try {
            HttpResponse response = sendPostRequest(messageUrl, request);
            if ((response.statusCode >= 200 && response.statusCode < 300)) {
                return objectMapper.readValue(response.body, Message.class);

            } else {
                System.err.println("Failed to Send a message. Status: " + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to register user", e);

        }
    }


}

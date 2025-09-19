package service;

import model.Conversation;
import model.Message;
import model.User;
import request.ConversationRequest;
import request.MessageRequest;

import java.io.IOException;

public class MessageApiClient implements ApiClient {

    String baseUrl = ("http://localhost:8080/api/conversations");

    public MessageApiClient() {
    }


    public Message sendMessage(MessageRequest request, Conversation conversation) {
        try {
            String messageUrl = baseUrl + "/" + conversation.getId() + "/messages";
            ApiResponse response = sendPostRequest(messageUrl, request);
            if ((response.isSuccess())) {
                return objectMapper.readValue(response.body, Message.class);
            } else {
                System.out.println("Failed to Send a message. Status: "
                        + response.statusCode + ", Response: " + response.body);
                return null;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to register user", e);

        }
    }

    public void deleteMessage(Conversation conversation, Message message, User user) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + conversation.getId() + "/messages/" + message.getId();
        String token = user.getToken();
        ApiResponse response = sendDeleteRequest(messageUrl, token);
        if ((response.isSuccess())) {
            System.out.println(response.body);
        } else {
            System.out.println("Failed to delete Message: "
                    + response.statusCode + ", Response: " + response.body);
        }
    }


}

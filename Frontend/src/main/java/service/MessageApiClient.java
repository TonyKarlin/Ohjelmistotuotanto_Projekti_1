package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Conversation;
import model.Message;
import model.User;
import request.MessageRequest;
import utils.ApiUrl;

import java.io.IOException;
import java.util.List;

public class MessageApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/conversations";

    public MessageApiClient() {
    }

    public Message sendMessage(MessageRequest request) {
        try {
            String messageUrl = baseUrl + "/" + request.getConversationId() + "/messages";
            String token = request.getToken();
            ApiResponse response = sendPostRequestWithToken(messageUrl, request, token);
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

    public Message modifyMessage(MessageRequest request) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + request.getConversationId() +
                "/messages/" + request.getMessageId();
        String token = request.getToken();
        ApiResponse response = sendPutRequestWithObjectAndToken(messageUrl, request, token);
        if (response.isSuccess()) {
            System.out.println(response.body);
            return objectMapper.readValue(response.body, Message.class);
        } else {
            System.out.println("Failed to Modify  message. Status: "
                    + response.statusCode + ", Response: " + response.body);
            return null;
        }
    }

    public List<Message> getConversationMessages(Conversation conversation, User user) throws IOException, InterruptedException {
        String token = user.getToken();
        String messageUrl = baseUrl + "/" + conversation.getId() + "/messages";
        ApiResponse response = sendGetRequest(messageUrl, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Message>>() {
            });
        } else {
            System.out.println("Failed to get conversation: " + conversation.getId() + " "
                    + "messages" + response.statusCode + ", Response: " + response.body);
            return null;
        }
    }

    public void deleteMessage(MessageRequest request) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + request.getConversationId() + "/messages/" + request.getMessageId();
        String token = request.getToken();
        ApiResponse response = sendDeleteRequestWithToken(messageUrl, token);
        if ((response.isSuccess())) {
            System.out.println(response.body);
        } else {
            System.out.println("Failed to delete Message: "
                    + response.statusCode + ", Response: " + response.body);
        }
    }

}

package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Conversation;
import model.User;
import request.ConversationRequest;

import java.io.IOException;
import java.util.List;

public class ConversationApiClient implements ApiClient {

    String baseUrl = "http://localhost:8080/api/conversations";

    public ConversationApiClient() {

    }

    public Conversation createConversation(ConversationRequest conversationRequest) throws IOException, InterruptedException {
        String conversationUrl = baseUrl;
        ApiResponse response = sendPostRequest(conversationUrl, conversationRequest);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, Conversation.class);
        } else {
            System.out.println("Failed to start conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }

    public List<Conversation> getAllConversations() throws IOException, InterruptedException {
        ApiResponse response = sendGetRequest(baseUrl);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, new TypeReference<List<Conversation>>() {
            });
        } else {
            System.out.println("Failed to start conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }

    }

    public List<Conversation> getConversationsById(User user) throws IOException, InterruptedException {
        String conversationUrl = (baseUrl + "/user/" + user.getId());
        ApiResponse response = sendGetRequest(conversationUrl);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, new TypeReference<List<Conversation>>() {
            });
        } else {
            System.out.println("Failed to start conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }

    }

}

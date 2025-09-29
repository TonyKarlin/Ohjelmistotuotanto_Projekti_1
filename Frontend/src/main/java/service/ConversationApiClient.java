package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Contact;
import model.Conversation;
import model.ConversationParticipant;
import model.User;
import request.ConversationRequest;
import utils.ApiUrl;

import java.io.IOException;
import java.util.List;

public class ConversationApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/conversations";

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

    public Conversation changeConversationName(ConversationRequest request) throws IOException, InterruptedException {
        String conversationUrl = baseUrl + "/" + request.getConversationId() + "/update";
        ApiResponse response = sendPutRequestWithObjectAndToken(conversationUrl, request, request.getToken());
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, Conversation.class);
        }
        System.out.println("Failed to change conversation name. Status: " + response.statusCode
                + ", Response: " + response.body);
        return null;
    }

    public void addUserToConversation(Conversation conversation, Contact Contact) throws IOException, InterruptedException {
        String conversationUrl = baseUrl + "/" + conversation.getId() + "/participants/" + Contact.getContactUserId();
        ApiResponse response = sendPutRequestWithoutObject(conversationUrl);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);

        } else {
            System.out.println("Failed to add user to conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
        }
    }

    public void leaveConversation(Conversation conversation, User user) throws IOException, InterruptedException {
        String conversationUrl = baseUrl + "/" + conversation.getId() + "/leave?userId=" + user.getId();
        ApiResponse response = sendPatchRequest(conversationUrl);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);

        } else {
            System.out.println("Failed to leave conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
        }
    }

    public boolean deleteConversation(Conversation conversation, User user) throws IOException, InterruptedException {
        String conversationUrl = baseUrl + "/" + conversation.getId() + "?requesterId=" + user.getId();
        String token = user.getToken();
        ApiResponse response = sendDeleteRequestWithToken(conversationUrl, token);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return true;

        } else {
            System.out.println("Failed to delete conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
        }return false;
    }

    public boolean removeUserFromConversation(int participantId, int conversationId, String token) throws IOException, InterruptedException {
        String conversationUrl = baseUrl + "/" + conversationId + "/participants/" + participantId;
        System.out.println(conversationUrl);
        ApiResponse response = sendDeleteRequestWithToken(conversationUrl, token);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return true;

        } else {
            System.out.println("Failed to remove User from conversation. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return false;
        }
    }

    public List<Conversation> getAllUserConversations(User user) throws IOException, InterruptedException {
        String url = baseUrl +"/user/me";
        String token = user.getToken();
        ApiResponse response = sendGetRequest(url, token);
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
        String token = user.getToken();
        ApiResponse response = sendGetRequest(conversationUrl, token);
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

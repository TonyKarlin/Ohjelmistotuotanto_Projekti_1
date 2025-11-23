package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Conversation;
import model.User;
import request.ConversationRequest;
import utils.ApiUrl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConversationApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/conversations";
    String stringResponse = ", Response: ";
    private static final Logger logger = Logger.getLogger(ConversationApiClient.class.getName());

    public Conversation createConversation(ConversationRequest conversationRequest, String token) throws JsonProcessingException {
        String conversationUrl = baseUrl;
        ApiResponse response = sendPostRequestWithToken(conversationUrl, conversationRequest, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, Conversation.class);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to start conversation. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return null;
        }
    }

    public Conversation changeConversationName(ConversationRequest request) throws JsonProcessingException {
        String conversationUrl = baseUrl + "/" + request.getConversationId() + "/update";
        ApiResponse response = sendPutRequestWithObjectAndToken(conversationUrl, request, request.getToken());
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, Conversation.class);
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format(
                    "Failed to change conversation name. Status: %d, Response: %s",
                    response.statusCode,
                    response.body
            ));
        }
        return null;
    }

    public boolean addUserToConversation(int conversationId, int contactId, String token) {
        String conversationUrl = baseUrl + "/" + conversationId + "/participants/" + contactId;
        ApiResponse response = sendPutRequestWithoutObject(conversationUrl, token);
        if (response.isSuccess()) {
            return true;
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to add user to conversation. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return false;
        }
    }

    public boolean leaveConversation(Conversation conversation, User user) throws JsonProcessingException {
        String conversationUrl = baseUrl + "/" + conversation.getId() + "/leave?userId=" + user.getId();
        ApiResponse response = sendPatchRequest(conversationUrl);
        if (response.isSuccess()) {
            return true;
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to leave conversation. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return false;
        }
    }

    public boolean deleteConversation(Conversation conversation, User user) throws JsonProcessingException {
        String conversationUrl = baseUrl + "/" + conversation.getId() + "?requesterId=" + user.getId();
        String token = user.getToken();
        ApiResponse response = sendDeleteRequestWithToken(conversationUrl, token);
        if (response.isSuccess()) {
            return true;
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to delete conversation. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
        }
        return false;
    }

    public boolean removeUserFromConversation(int participantId, int conversationId, String token) throws JsonProcessingException {
        String conversationUrl = baseUrl + "/" + conversationId + "/participants/" + participantId;
        ApiResponse response = sendDeleteRequestWithToken(conversationUrl, token);
        if (response.isSuccess()) {
            return true;
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to remove User from conversation. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return false;
        }
    }

    public List<Conversation> getAllUserConversations(User user) throws JsonProcessingException {
        String url = baseUrl + "/user/me";
        String token = user.getToken();
        ApiResponse response = sendGetRequest(url, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Conversation>>() {
            });
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to get user conversations. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return Collections.emptyList();
        }

    }

    public List<Conversation> getConversationsById(User user) throws JsonProcessingException {
        String conversationUrl = (baseUrl + "/user/" + user.getId());
        String token = user.getToken();
        ApiResponse response = sendGetRequest(conversationUrl, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Conversation>>() {
            });
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to get conversation by its id. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return Collections.emptyList();
        }

    }

}

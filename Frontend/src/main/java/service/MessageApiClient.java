package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import model.Conversation;
import model.Message;
import model.User;
import request.MessageRequest;
import utils.ApiUrl;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/conversations";
    String stringResponse = ", Response: ";
    private static final Logger logger = Logger.getLogger(MessageApiClient.class.getName());

    public Message sendMessage(MessageRequest request) throws JsonProcessingException {
        String messageUrl = baseUrl + "/" + request.getConversationId() + "/messages";
        String token = request.getToken();
        ApiResponse response = sendPostRequestWithToken(messageUrl, request, token);
        if ((response.isSuccess())) {
            return objectMapper.readValue(response.body, Message.class);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to send a message. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return null;
        }
    }

    public Message modifyMessage(MessageRequest request, int messageId) throws JsonProcessingException {
        String messageUrl = baseUrl + "/" + request.getConversationId()
                + "/messages/" + messageId;
        String token = request.getToken();
        ApiResponse response = sendPutRequestWithObjectAndToken(messageUrl, request, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, Message.class);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to modify message. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
            return null;
        }
    }

    public List<Message> getConversationMessages(Conversation conversation, User user) throws JsonProcessingException {
        String token = user.getToken();
        String messageUrl = baseUrl + "/" + conversation.getId() + "/messages";
        ApiResponse response = sendGetRequest(messageUrl, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Message>>() {
            });
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to get conversation %d messages. Status: %d, Response: %s",
                        conversation.getId(),
                        response.statusCode,
                        response.body
                ));
            }
            return Collections.emptyList();
        }
    }

    public boolean deleteMessage(int conversationId, int messageId, String token) {
        String messageUrl = baseUrl + "/" + conversationId + "/messages/" + messageId;
        ApiResponse response = sendDeleteRequestWithToken(messageUrl, token);
        if ((response.isSuccess())) {
            return true;
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info(String.format(
                        "Failed to delete message. Status: %d, Response: %s",
                        response.statusCode,
                        response.body
                ));
            }
        }
        return false;

    }

}

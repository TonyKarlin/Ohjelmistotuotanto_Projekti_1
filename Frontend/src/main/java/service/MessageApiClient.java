package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Conversation;
import model.Message;
import model.User;
import request.ConversationRequest;
import request.MessageRequest;

import java.io.IOException;
import java.util.List;

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

    public Message modifyMessage(MessageRequest messageRequest) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + messageRequest.getConversationId() + "/messages/" + messageRequest.getMessageId();
        ApiResponse response = sendPutRequestWithObject(messageUrl, messageRequest);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, Message.class);
        }else {
            System.out.println("Failed to Modify  message. Status: "
                    + response.statusCode + ", Response: " + response.body);
            return null;
        }
    }

    public List<Message> getConversationMessages(Conversation conversation) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + conversation.getId() + "/messages";
        ApiResponse response = sendGetRequest(messageUrl);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Message>>() {
            });
        } else {
            System.out.println("Failed to get conversation: "+ conversation.getId() +" " +
                    "messages" +response.statusCode +", Response: " + response.body);
            return null;
        }
    }

    public void deleteMessage(Conversation conversation, Message message, User user) throws IOException, InterruptedException {
        String messageUrl = baseUrl + "/" + conversation.getId() + "/messages/" + message.getId()+"?userId="+user.getId();
        String token = user.getToken();
        ApiResponse response = sendDeleteRequestWithToken(messageUrl, token);
        if ((response.isSuccess())) {
            System.out.println(response.body);
        } else {
            System.out.println("Failed to delete Message: "
                    + response.statusCode + ", Response: " + response.body);
        }
    }


}

package service;

import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import controller.ConversationSettingsController;
import model.Contact;
import model.ContactResponse;
import model.User;
import request.ContactRequest;
import utils.ApiUrl;


public class ContactApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/contacts";
    String stringResponse = ", Response: ";
    private static final Logger logger = Logger.getLogger(ContactApiClient.class.getName());


    // Get a list of ALL contacts PENDING and ACCEPTED
    public List<Contact> getAllUserContacts(User user) throws JsonProcessingException {
        String token = user.getToken();
        ApiResponse response = sendGetRequest(baseUrl, token);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, new TypeReference<List<Contact>>() {
            });
        } else {
           logger.info("Failed to get Contacts. Status: " + response.statusCode
                    + stringResponse + response.body);
            return null;
        }
    }

    public Contact addContact(ContactRequest contactRequest) throws JsonProcessingException {
        String contactUrl = baseUrl + "/add?userId=" + contactRequest.getUserId() + "&contactUserId=" + contactRequest.getContactUserId();
        String token = contactRequest.getToken();
        ApiResponse response = sendPostRequestWithToken(contactUrl, contactRequest, token); // Changed this line
        if (response.isSuccess()) {
            return objectMapper.readValue(response.body, Contact.class);
        } else {
            logger.info("Failed to add contact. Status: " + response.statusCode
                    + stringResponse + response.body);
            return null;
        }
    }

    public Contact acceptContact(ContactRequest contactRequest) throws JsonProcessingException {
        String contactUrl = baseUrl + "/accept?userId=" + contactRequest.getUserId() + "&contactUserId=" + contactRequest.getContactUserId();
        String token = contactRequest.getToken(); // Add this line to get the token
        ApiResponse response = sendPostRequestWithToken(contactUrl, contactRequest, token); // Changed this line
        if (response.isSuccess()) {
            ContactResponse contactResponse = objectMapper.readValue(response.body, ContactResponse.class);
            return contactResponse.getContact();
        } else {
            logger.info("Failed to add contact. Status: " + response.statusCode
                    + stringResponse + response.body);
            return null;
        }
    }
}

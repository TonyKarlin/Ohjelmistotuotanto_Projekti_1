package service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import model.Contact;
import model.ContactResponse;
import model.User;
import request.ContactRequest;
import utils.ApiUrl;

public class ContactApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/contacts";

    public ContactApiClient() {

    }

    // Get a list of ALL contacts PENDING and ACCEPTED
    public List<Contact> getAllUserContacts(User user) throws IOException, InterruptedException {
        String token = user.getToken();
        ApiResponse response = sendGetRequest(baseUrl, token);
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, new TypeReference<List<Contact>>() {
            });
        } else {
            System.out.println("Failed to get Contacts. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }

    public Contact addContact(ContactRequest contactRequest) throws IOException, InterruptedException {
        String contactUrl = baseUrl + "/add?userId=" + contactRequest.getUserId() + "&contactUserId=" + contactRequest.getContactUserId();
        String token = contactRequest.getToken();

        ApiResponse response = sendPostRequestWithToken(contactUrl, contactRequest, token); // Changed this line
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, Contact.class);
        } else {
            System.out.println("Failed to add contact. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }

    public Contact acceptContact(ContactRequest contactRequest) throws IOException, InterruptedException {
        String contactUrl = baseUrl + "/accept?userId=" + contactRequest.getUserId() + "&contactUserId=" + contactRequest.getContactUserId();
        String token = contactRequest.getToken(); // Add this line to get the token

        ApiResponse response = sendPostRequestWithToken(contactUrl, contactRequest, token); // Changed this line
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            ContactResponse contactResponse = objectMapper.readValue(response.body, ContactResponse.class);
            return contactResponse.getContact();
        } else {
            System.out.println("Failed to add contact. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }
}

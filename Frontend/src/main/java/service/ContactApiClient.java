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

    // Get a list of ACCEPTED contacts(friends) of the current user
    public List<Contact> getAllUserContacts(User user) throws IOException, InterruptedException {
        ApiResponse response = sendGetRequest(baseUrl + "/user/" + user.getId() + "/accepted");
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

    // Get a list of PENDING contacts of the current user
    public List<Contact> getAllPendingUserContacts(User user) throws IOException, InterruptedException {
        ApiResponse response = sendGetRequest(baseUrl + "/user/" + user.getId() + "/pending");
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, new TypeReference<List<Contact>>() {
            });
        } else {
            System.out.println("Failed to get pending Contacts. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }

    // Get a list of SENT contacts of the current user
    public List<Contact> getAllSentUserContacts(User user) throws IOException, InterruptedException {
        ApiResponse response = sendGetRequest(baseUrl + "/user/" + user.getId() + "/sent");
        if (response.isSuccess()) {
            System.out.println("Response: " + response.body);
            return objectMapper.readValue(response.body, new TypeReference<List<Contact>>() {
            });
        } else {
            System.out.println("Failed to get pending Contacts. Status: " + response.statusCode
                    + ", Response: " + response.body);
            return null;
        }
    }

    public Contact addContact(ContactRequest contactRequest) throws IOException, InterruptedException {

        String contactUrl = baseUrl + "/add?userId=" + contactRequest.getUserId() + "&contactUserId=" + contactRequest.getContactUserId();

        String jsonBody = objectMapper.writeValueAsString(contactRequest);
        ApiResponse response = sendPostRequest(contactUrl, jsonBody);
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

        String jsonBody = objectMapper.writeValueAsString(contactRequest);
        ApiResponse response = sendPostRequest(contactUrl, jsonBody);
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

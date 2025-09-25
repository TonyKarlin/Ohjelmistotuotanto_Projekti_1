package service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import model.Contact;
import model.User;
import utils.ApiUrl;

public class ContactApiClient implements ApiClient {

    String baseUrl = ApiUrl.getApiUrl() + "/contacts";

    public ContactApiClient() {

    }

    // Get a list of contacts(friends) of the current user
    public List<Contact> getAllUserContacts(User user) throws IOException, InterruptedException {
        ApiResponse response = sendGetRequest(baseUrl + "/" + user.getId());
        System.out.println(baseUrl + "/" + user.getId());
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
}

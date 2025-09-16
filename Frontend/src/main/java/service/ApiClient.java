package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Interface jota pystyy hyödyntää kaikissa service luokan api pyynnöissä.

public interface ApiClient {

    ObjectMapper objectMapper = new ObjectMapper();


    //Method needs only a right url and object as parameters
    default ApiResponse sendPostRequest(String urlString, Object body) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(urlString))
                .header("Content-Type", "Application/json")
                .build();

        java.net.http.HttpResponse<String> response = HttpClient.newHttpClient().send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
    }

    default void sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
    }

    default ApiResponse sendDeleteRequest(String urlString, String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(urlString))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());

    }
}


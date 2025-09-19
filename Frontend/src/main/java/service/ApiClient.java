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
        try {
            String requestBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder().
                    POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(URI.create(urlString))
                    .header("Content-Type", "Application/json")
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().
                    send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    default ApiResponse sendGetRequest(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().
                GET()
                .uri(URI.create(urlString))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());
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


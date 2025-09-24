package service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//This Interface can be used multiple Api client requests

public interface ApiClient {

    ObjectMapper objectMapper = new ObjectMapper();


    //Post Method needs only a right url and object as parameters
    default ApiResponse sendPostRequest(String urlString, Object body) throws IOException, InterruptedException {
        try {
            //Turns object to json data
            String requestBody = objectMapper.writeValueAsString(body);

            // Create an HTTP POST request with JSON body and Content-Type header
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(URI.create(urlString))
                    .header("Content-Type", "Application/json")
                    .build();

            // Send the request and store the response body as a String
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //return api response that saves the response body and status code
            return new ApiResponse(response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    //Get Method needs only right url
    default ApiResponse sendGetRequest(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlString))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());
    }

    default ApiResponse sendPutRequestWithObject(String urlString, Object body) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(urlString))
                .header("Content-Type", "Application/json")
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());

    }

    default ApiResponse sendPutRequestOnlyUrl(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(urlString))
                .header("Content-Type", "Application/json")
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());
    }

    default ApiResponse sendPatchRequest(String urlString) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(urlString))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return new ApiResponse(response.statusCode(), response.body());
    }


    //Delete Method needs only right url
    default ApiResponse sendDeleteRequest(String urlString, String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(urlString))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());

    }
}


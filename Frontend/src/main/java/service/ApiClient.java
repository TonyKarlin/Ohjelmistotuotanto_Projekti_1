package service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

//This Interface can be used multiple Api client requests
public interface ApiClient {

    ObjectMapper objectMapper = new ObjectMapper();
    HttpClient httpClient = HttpClient.newHttpClient();

    //Post Method needs only a right url and object as parameters
    default ApiResponse sendPostRequest(String urlString, Object body) {
        try {
            //Turns object to json data
            String requestBody = objectMapper.writeValueAsString(body);

            // Create an HTTP POST request with JSON body and Content-Type header
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .build();
            // Send the request and store the response body as a String
            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            //return api response that saves the response body and status code
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    default ApiResponse sendPostRequestWithToken(String urlString, Object body, String token) {
        try {
            String requestBody = objectMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(URI.create(urlString))
                    .header("Content-Type", "Application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();
            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    //Get Method needs only right url
    default ApiResponse sendGetRequest(String urlString, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(urlString))
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    default ApiResponse sendPutRequestWithObjectAndToken(String urlString, Object body, String token) {
        try {
            String requestBody = objectMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .uri(URI.create(urlString))
                    .header("Content-Type", "Application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }

    }

    default ApiResponse sendPutRequestWithoutObject(String urlString, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(urlString))
                    .header("Content-Type", "Application/json")
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    default ApiResponse sendPatchRequest(String urlString) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(urlString))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }
    }

    //Delete Method needs only right url and token
    default ApiResponse sendDeleteRequestWithToken(String urlString, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create(urlString))
                    .header("Authorization", "Bearer " + token)
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }

    }

    //Delete Method without token
    default ApiResponse sendDeleteRequestWithoutToken(String urlString) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create(urlString))
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ApiResponse(499, "Request interrupted: " + e.getMessage());
        } catch (IOException e) {
            return new ApiResponse(503, "Service unavailable: " + e.getMessage());
        }

    }

    default ApiResponse sendFile(String urlString, File file, String token) {
        FileSystemResource fileResource = new FileSystemResource(file);
        String responseBody = WebClient.create()
                .post()
                .uri(urlString)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", fileResource))
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ApiResponse(200, responseBody);

    }
}

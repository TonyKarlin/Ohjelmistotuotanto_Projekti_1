package service;


import lombok.Data;

@Data
public class HttpResponse {
    int statusCode;
    String body;

    HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}

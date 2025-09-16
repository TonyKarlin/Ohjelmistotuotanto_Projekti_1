package service;


import lombok.Data;

@Data
public class ApiResponse {
    int statusCode;
    String body;

    ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode <300;
    }
}

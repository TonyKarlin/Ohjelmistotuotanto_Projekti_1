package service;


import lombok.Data;

//object to save the status code and response body

@Data
public class ApiResponse {
    int statusCode;
    String body;

    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    //Checker if request is success
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode <300;
    }


}

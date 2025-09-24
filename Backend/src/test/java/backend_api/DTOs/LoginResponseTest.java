package backend_api.DTOs;

import backend_api.DTOs.user.LoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void getMessage() {
        LoginResponse response = new LoginResponse("Success");
        assertEquals("Success", response.getMessage());
    }

    @Test
    void getToken() {
        LoginResponse response = new LoginResponse("Success", "Token", 1L, "Username", "Email");
        assertEquals("Token", response.getToken());
    }
}
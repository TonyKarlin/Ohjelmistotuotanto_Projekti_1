package backend_api.DTOs;

import backend_api.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void getUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("Test");
        assertEquals("Test", request.getUsername());
    }

    @Test
    void getPassword() {
        LoginRequest request = new LoginRequest();
        request.setPassword("Password123");
        assertEquals("Password123", request.getPassword());
    }
}
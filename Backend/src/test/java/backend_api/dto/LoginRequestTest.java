package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import backend_api.dto.user.LoginRequest;

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

package backend_api.DTOs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void getUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Test");
        assertEquals("Test", request.getUsername());
    }

    @Test
    void password() {
        RegisterRequest request = new RegisterRequest();
        request.setPassword("Test");
        assertEquals("Test", request.getPassword());
    }

    @Test
    void email() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("Test@Test.com");
        assertEquals("Test@Test.com", request.getEmail());
    }
}
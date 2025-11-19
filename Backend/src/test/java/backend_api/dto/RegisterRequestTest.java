package backend_api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import backend_api.dto.user.RegisterRequest;

class RegisterRequestTest {

    @Test
    void testUsername() {
        RegisterRequest request = new RegisterRequest(
                "Test",
                "12345",
                "Test@example.com",
                "en");

        assertEquals("Test", request.username());
    }

    @Test
    void testPassword() {
        RegisterRequest request = new RegisterRequest(
                "Test",
                "12345",
                "Test@example.com",
                "en");
        assertEquals("12345", request.password());
    }

    @Test
    void testEmail() {
        RegisterRequest request = new RegisterRequest(
                "Test",
                "12345",
                "Test@example.com",
                "en");
        assertEquals("Test@example.com", request.email());
    }

    @Test
    void testLanguage() {
        RegisterRequest request = new RegisterRequest(
                "Test",
                "12345",
                "Test@example.com",
                "en");

        assertEquals("en", request.language());
    }
}

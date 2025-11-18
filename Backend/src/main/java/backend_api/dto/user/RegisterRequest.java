package backend_api.dto.user;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String language
) {}

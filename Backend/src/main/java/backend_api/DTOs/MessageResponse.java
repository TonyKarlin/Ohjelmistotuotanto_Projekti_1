package backend_api.DTOs;

public record MessageResponse(Long messageId, Long userId, String message) {
}

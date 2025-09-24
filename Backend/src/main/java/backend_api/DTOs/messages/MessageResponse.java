package backend_api.DTOs.messages;

public record MessageResponse(Long messageId, Long userId, String message) {
}

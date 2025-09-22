package backend_api.DTOs;

public record ConversationParticipantResponse(Long conversationId, Long userId, String message) {
}

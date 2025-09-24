package backend_api.DTOs.conversations;

public record ConversationParticipantResponse(Long conversationId, Long userId, String message) {
}

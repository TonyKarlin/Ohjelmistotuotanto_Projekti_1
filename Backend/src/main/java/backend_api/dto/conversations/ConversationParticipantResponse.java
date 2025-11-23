package backend_api.dto.conversations;

public record ConversationParticipantResponse(Long conversationId, Long userId, String message) {

}

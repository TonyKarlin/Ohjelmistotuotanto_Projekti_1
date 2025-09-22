package backend_api.DTOs;

import backend_api.entities.Conversation;

public record AcceptContactDTO(ContactResponseDTO contact, Conversation conversation) {
}

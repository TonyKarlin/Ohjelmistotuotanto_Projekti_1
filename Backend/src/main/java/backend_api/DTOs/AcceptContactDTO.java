package backend_api.DTOs;

import backend_api.entities.Conversation;

// DTO to encapsulate the response for accepting a contact request
// Contains the contact details and the associated conversation
// Object can be used to return both pieces of information in a single response
// Useful for client applications to handle contact acceptance and conversation initiation seamlessly
public record AcceptContactDTO(ContactResponseDTO contact, Conversation conversation) {
}

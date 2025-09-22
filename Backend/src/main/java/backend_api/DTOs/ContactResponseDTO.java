package backend_api.DTOs;

import backend_api.enums.ContactStatus;

public record ContactResponseDTO(Long contactId, Long contactUserId, String contactUsername,
                                 ContactStatus status) {

}

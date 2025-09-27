package backend_api.DTOs.contacts;

import backend_api.enums.ContactStatus;

public record ContactResponseDTO(Long id, Long contactUserId, String contactUsername,
                                 ContactStatus status) {

}

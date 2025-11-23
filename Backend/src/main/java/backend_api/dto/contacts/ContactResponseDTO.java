package backend_api.dto.contacts;

import backend_api.enums.ContactStatus;

// ContactUserId == Friends UserId that is being added
// ContactUsername == Friends Username that is being added
// Status == Status of the contact (PENDING, ACCEPTED, etc.)
public record ContactResponseDTO(Long contactUserId, String contactUsername, ContactStatus status, Long initiatorId) {

}

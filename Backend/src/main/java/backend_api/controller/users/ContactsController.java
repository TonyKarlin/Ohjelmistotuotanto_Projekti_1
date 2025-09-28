package backend_api.controller.users;

import backend_api.DTOs.contacts.AcceptContactDTO;
import backend_api.DTOs.contacts.ContactResponseDTO;
import backend_api.entities.User;
import backend_api.services.ContactsService;
import backend_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @PostMapping("/add")
    public ResponseEntity<ContactResponseDTO> addContact(
            @RequestParam Long contactUserId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        ContactResponseDTO response = contactsService.addContact(authUser.getId(), contactUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept")
    public ResponseEntity<AcceptContactDTO> acceptContact(
            @RequestParam Long contactUserId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        AcceptContactDTO response = contactsService.acceptContact(authUser.getId(), contactUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteContact(
            @RequestParam Long contactUserId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        String response = contactsService.deleteContact(authUser.getId(), contactUserId);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping
    public ResponseEntity<List<ContactResponseDTO>> getContacts(Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();

        List<ContactResponseDTO> contacts = contactsService.getContacts(authUser.getId())
                .stream()
                .map(c -> contactsService.convertToDTO(c, authUser))
                .toList();

        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/contact/{contactUserId}")
    public ResponseEntity<ContactResponseDTO> getContactById(
            @PathVariable Long contactUserId,
            Authentication authentication) {

        User authUser = (User) authentication.getPrincipal();
        ContactResponseDTO response = contactsService.getContactByUserId(authUser.getId(), contactUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accepted")
    public ResponseEntity<List<ContactResponseDTO>> getAcceptedContacts(Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        List<ContactResponseDTO> contacts = contactsService.getAcceptedContacts(authUser.getId());
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ContactResponseDTO>> getPendingContacts(Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        List<ContactResponseDTO> contacts = contactsService.getPendingContacts(authUser.getId());
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<ContactResponseDTO>> getSentRequests(Authentication authentication) {
        User authUser = (User) authentication.getPrincipal();
        List<ContactResponseDTO> sentRequests = contactsService.getSentRequests(authUser.getId());
        return ResponseEntity.ok(sentRequests);
    }
}

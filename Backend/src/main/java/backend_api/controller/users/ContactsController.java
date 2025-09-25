package backend_api.controller.users;

import backend_api.DTOs.contacts.AcceptContactDTO;
import backend_api.DTOs.contacts.ContactResponseDTO;
import backend_api.services.ContactsService;
import backend_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

    private final ContactsService contactsService;
    private final UserService userService;

    public ContactsController(ContactsService contactsService, UserService userService) {
        this.contactsService = contactsService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<ContactResponseDTO> addContact(@RequestParam Long userId, @RequestParam Long contactUserId) {
        ContactResponseDTO response = contactsService.addContact(userId, contactUserId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept")
    public ResponseEntity<AcceptContactDTO> acceptContact(@RequestParam Long userId, @RequestParam Long contactUserId) {
        AcceptContactDTO response = contactsService.acceptContact(userId, contactUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactResponseDTO>> getContacts(@PathVariable Long userId) {
        List<ContactResponseDTO> contacts = contactsService.getContacts(userId)
                .stream()
                .map(c -> contactsService.convertToDTO(c,
                        userService.getUserOrThrow(userId)))
                .toList();
        return ResponseEntity.ok(contacts);
    }


    @GetMapping("/user/{userId}/contact/{contactUserId}")
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long userId, @PathVariable Long contactUserId) {
        ContactResponseDTO response = contactsService.getContactByUserId(userId, contactUserId);
        return ResponseEntity.ok(response);
    }
}

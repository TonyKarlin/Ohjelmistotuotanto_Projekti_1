package backend_api.controller;

import backend_api.DTOs.AcceptContactDTO;
import backend_api.DTOs.ContactResponseDTO;
import backend_api.services.ContactsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactsController {

    private final ContactsService contactsService;

    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
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

    @GetMapping("/{userId}")
    public ResponseEntity<List<ContactResponseDTO>> getContacts(@PathVariable Long userId) {
        List<ContactResponseDTO> contacts = contactsService.getContacts(userId)
                .stream()
                .map(contactsService::convertToDTO)
                .toList();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping
    public ResponseEntity<ContactResponseDTO> getContactById(@RequestParam Long userId, @RequestParam Long contactUserId) {
        ContactResponseDTO response = contactsService.getContactByUserId(userId, contactUserId);
        return ResponseEntity.ok(response);
    }
}

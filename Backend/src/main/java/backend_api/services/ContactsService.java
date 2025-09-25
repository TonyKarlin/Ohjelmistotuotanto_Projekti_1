package backend_api.services;

import backend_api.DTOs.contacts.AcceptContactDTO;
import backend_api.DTOs.contacts.ContactResponseDTO;
import backend_api.DTOs.conversations.ConversationDTO;
import backend_api.entities.Contacts;
import backend_api.entities.Conversation;
import backend_api.entities.User;
import backend_api.enums.ContactStatus;
import backend_api.repository.ContactsRepository;
import backend_api.repository.UserRepository;
import backend_api.utils.customexceptions.ContactAlreadyExistsException;
import backend_api.utils.customexceptions.InvalidStatusException;
import backend_api.utils.customexceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ContactsService {
    private final ContactsRepository contactsRepository;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    public ContactsService(ContactsRepository contactsRepository, UserRepository userRepository, ConversationService conversationService) {
        this.contactsRepository = contactsRepository;
        this.userRepository = userRepository;
        this.conversationService = conversationService;
    }

    public ContactResponseDTO convertToDTO(Contacts contact, User currentUser) {
        User contactUser = contact.getUserId().equals(currentUser)
                ? contact.getContactId()
                : contact.getUserId();

        return new ContactResponseDTO(
                contact.getId(),
                contactUser.getId(),      // Contact user ID
                contactUser.getUsername(),// Contact username
                contact.getStatus()
        );
    }

    public List<Contacts> fetchAllContacts(User user) {
        List<Contacts> sent = contactsRepository.findAllByUser(user);
        List<Contacts> received = contactsRepository.findAllByContact(user);

        List<Contacts> allContacts = new ArrayList<>();
        allContacts.addAll(sent);
        allContacts.addAll(received);
        return allContacts;
    }

    private Contacts searchForExistingContact(User user, User contactUser) {
        return contactsRepository.findByUserAndContact(contactUser, user)
                .or(() -> contactsRepository.findByUserAndContact(user, contactUser))
                .orElse(null);
    }

    private Contacts createContactBetweenUsers(User user, User contactUser) {
        Contacts newContact = new Contacts();
        newContact.setUserId(user);
        newContact.setContactId(contactUser);
        newContact.setStatus(ContactStatus.PENDING); // Default status, can be changed later
        return contactsRepository.save(newContact);
    }

    public ContactResponseDTO addContact(Long userId, Long contactUserId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        User contactUser = userRepository.findById(contactUserId).orElseThrow(() ->
                new UserNotFoundException("Contact user not found with id: " + contactUserId));

        contactsRepository.findByUserAndContact(user, contactUser).ifPresent(contact -> {
            throw new ContactAlreadyExistsException("Contact already exists between user " + userId +
                    " and contact " + contactUserId);
        });

        Contacts contact = createContactBetweenUsers(user, contactUser);
        return convertToDTO(contact, user);
    }

    public AcceptContactDTO acceptContact(Long userId, Long contactUserId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        User contactUser = userRepository.findById(contactUserId).orElseThrow(() ->
                new UserNotFoundException("Contact user not found with id: " + contactUserId));

        Contacts contact = searchForExistingContact(user, contactUser);

        contact.setStatus(ContactStatus.ACCEPTED);
        contactsRepository.save(contact);

        Conversation conversation = conversationService.createPrivateConversationForNewContacts(user, contactUser);
        ConversationDTO dto = ConversationDTO.fromConversationEntity(conversation);

        return new AcceptContactDTO(convertToDTO(contact, user), dto);
    }

    public String deleteContact(Long userId, Long contactId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        User contactUser = userRepository.findById(contactId).orElseThrow(() ->
                new UserNotFoundException("Contact user not found with id: " + contactId));

        Contacts contact = searchForExistingContact(user, contactUser);

        String message;
        if (contact.getStatus() == ContactStatus.PENDING) {
            message = "Contact request declined";
        } else if (contact.getStatus() == ContactStatus.ACCEPTED) {
            message = "Contact removed";
        } else {
            throw new InvalidStatusException("Invalid contact status for declining/removing contact");
        }

        contactsRepository.delete(contact);
        return message;
    }

    public List<Contacts> getContacts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        return fetchAllContacts(user);
    }

    public ContactResponseDTO getContactByUserId(Long userId, Long contactId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        User contactUser = userRepository.findById(contactId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + contactId));

        Contacts contact = searchForExistingContact(user, contactUser);

        return convertToDTO(contact, user);
    }

}

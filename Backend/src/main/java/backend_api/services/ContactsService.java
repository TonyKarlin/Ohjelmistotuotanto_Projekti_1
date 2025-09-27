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
import backend_api.utils.customexceptions.InvalidContactRequestException;
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
        User contactUser = contact.getUser().getId().equals(currentUser.getId())
                ? contact.getContact()
                : contact.getUser();

        return new ContactResponseDTO(
                contact.getId(),            // Friendship ID
                contactUser.getId(),        // Friend's user ID
                contactUser.getUsername(),  // Friend's username
                contact.getStatus()         // Friendship status (PENDING, ACCEPTED, etc.)
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

    public void checkIfContactIsYourself(Long userId, Long contactUserId) {
        if (userId.equals(contactUserId)) {
            throw new InvalidContactRequestException("Cannot add yourself as a contact");
        }
    }

    private void checkForExistingContact(User user, User contactUser) {
        contactsRepository.findByUserAndContact(contactUser, user)
                .or(() -> contactsRepository.findByUserAndContact(user, contactUser))
                .ifPresent(c -> {
                    throw new ContactAlreadyExistsException("Contact already exists or request pending between users");
                });
    }

    private Contacts searchForExistingContact(User user, User contactUser) {
        return contactsRepository.findByUserAndContact(contactUser, user)
                .or(() -> contactsRepository.findByUserAndContact(user, contactUser))
                .orElse(null);
    }

    public Contacts searchForIncomingRequest(User user, User sender) {
        return contactsRepository.findByUserAndContact(sender, user)
                .orElse(null);
    }

    private Contacts createContactBetweenUsers(User user, User contactUser) {
        Contacts newContact = new Contacts();
        newContact.setUser(user);
        newContact.setContact(contactUser);
        newContact.setStatus(ContactStatus.PENDING); // Default status, can be changed later
        return contactsRepository.save(newContact);
    }

    public ContactResponseDTO addContact(Long userId, Long contactUserId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));
        User contactUser = userRepository.findById(contactUserId).orElseThrow(() ->
                new UserNotFoundException("Contact user not found with id: " + contactUserId));

        checkIfContactIsYourself(userId, contactUserId);
        checkForExistingContact(user, contactUser);

        Contacts contact = createContactBetweenUsers(user, contactUser);
        return convertToDTO(contact, user);
    }

    public AcceptContactDTO acceptContact(Long userId, Long contactUserId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        User contactUser = userRepository.findById(contactUserId).orElseThrow(() ->
                new UserNotFoundException("Contact user not found with id: " + contactUserId));


        Contacts contact = searchForIncomingRequest(user, contactUser);

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

    public List<ContactResponseDTO> getAcceptedContacts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        List<Contacts> allContacts = fetchAllContacts(user);

        List<Contacts> acceptedContacts = allContacts
                .stream()
                .filter(contact ->
                        contact.getStatus() == ContactStatus.ACCEPTED)
                .toList();

        return acceptedContacts
                .stream()
                .map(contact -> convertToDTO(contact, user))
                .toList();
    }

    // Only for the receiver of the contact request
    public List<ContactResponseDTO> getPendingContacts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId));

        List<Contacts> allContacts = fetchAllContacts(user);

        List<Contacts> pendingContactRequests = allContacts
                .stream()
                .filter(contact ->
                        contact.getStatus() == ContactStatus.PENDING && contact.getContact().equals(user))
                .toList();

        return pendingContactRequests
                .stream()
                .map(c -> convertToDTO(c, user))
                .toList();
    }


    // Only for the sender of the contact request
    public List<ContactResponseDTO> getSentRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Contacts> sentRequests = contactsRepository.findAllByUser(user).stream()
                .filter(contact -> contact.getStatus() == ContactStatus.PENDING)
                .toList();

        return sentRequests.stream()
                .map(contact -> convertToDTO(contact, user))
                .toList();
    }


}

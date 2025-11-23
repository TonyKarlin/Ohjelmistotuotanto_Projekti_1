package backend_api.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import backend_api.dto.user.UpdateUserRequest;
import backend_api.entities.User;
import backend_api.repository.UserRepository;
import backend_api.utils.customexceptions.InvalidUserException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidUserException(fieldName + " cannot be empty");
        }
    }

    private void validateUserForRegistration(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new InvalidUserException("Username already exists");
        }

        requireNonEmpty(user.getUsername(), "Username");
        requireNonEmpty(user.getPassword(), "Password");
        requireNonEmpty(user.getEmail(), "Email");
    }

    public User register(User user) {
        validateUserForRegistration(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    public List<User> getConversationParticipants(List<Long> participants) {
        return userRepository.findAllById(participants);
    }

    private boolean ifUsernameTaken(String username, Long id) {
        return userRepository.findByUsername(username)
                .filter(user -> !user.getId().equals(id))
                .isPresent();
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User user = getUserOrThrow(id);

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (ifUsernameTaken(request.getUsername(), id)) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(encodePassword(request.getPassword()));
        }

        if (request.getLanguage() != null && !request.getLanguage().isBlank()) {
            user.setLanguage(request.getLanguage());
        }

        return userRepository.save(user);
    }

    public void checkIfDirectoryExists(File dir) throws IOException {
        if (!dir.exists()) {
            boolean createdDirectories = dir.mkdirs();
            if (!createdDirectories) {
                throw new IOException("Failed to create upload directory");
            }
        }
    }

    public File setUpUploadDirectory() throws IOException {
        String serverDir = new File(".").getCanonicalPath(); // project root
        String uploadDir = serverDir + File.separator + "uploads";
        File dir = new File(uploadDir);

        checkIfDirectoryExists(dir);
        return dir;
    }

    public User updateProfilePicture(Long id, MultipartFile file) throws IOException {
        User user = getUserOrThrow(id);

        File uploadDir = setUpUploadDirectory();

        String filename = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destination = new File(uploadDir + File.separator + filename);
        file.transferTo(destination);

        user.setProfilePicture(filename);

        return userRepository.save(user);
    }

    public Resource getProfilePicture(String filename, Path filePath) throws IOException {
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("File not found or not readable: " + filename);
        }
        return resource;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}

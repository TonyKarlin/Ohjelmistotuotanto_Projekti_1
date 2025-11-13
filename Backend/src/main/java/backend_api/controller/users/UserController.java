package backend_api.controller.users;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import backend_api.DTOs.user.UserWithTokenDTO;
import backend_api.utils.customexceptions.UnauthorizedActionException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import backend_api.DTOs.user.UpdateUserRequest;
import backend_api.DTOs.user.UserDTO;
import backend_api.DTOs.user.LoginRequest;
import backend_api.DTOs.user.RegisterRequest;
import backend_api.entities.User;
import backend_api.services.UserService;
import backend_api.utils.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(UserDTO::new)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(new UserDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(value -> ResponseEntity.ok(new UserDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        user.setProfilePicture("default.png");

        if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
            user.setLanguage(request.getLanguage());
        } else {
            user.setLanguage("en");
        }

        User savedUser = userService.register(user);
        return ResponseEntity.ok(new UserDTO(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserWithTokenDTO> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new UnauthorizedActionException("Invalid username or password"));

        if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
            user.setLanguage(request.getLanguage());
            userService.save(user);
        }

        String token = JwtUtil.generateToken(user.getUsername());
        UserWithTokenDTO response = new UserWithTokenDTO(
                token,
                new UserDTO(user)
        );
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWithTokenDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            Authentication authorization) {

        // Get Authenticated user from Spring Security context
        User authUser = (User) authorization.getPrincipal();

        if (!authUser.getId().equals(id)) {
            throw new UnauthorizedActionException("You can only update your own profile");
        }

        User updatedUser = userService.updateUser(id, request);

        String newToken = JwtUtil.generateToken(updatedUser.getUsername());

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newToken)
                .body(new UserWithTokenDTO(newToken, new UserDTO(updatedUser)));
    }

    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        User authUser = (User) authentication.getPrincipal();
        if (!authUser.getId().equals(id)) {
            throw new UnauthorizedActionException("You can only update your own profile picture");
        }

        User user = userService.updateProfilePicture(id, file);

        return ResponseEntity.ok(new UserDTO(user));
    }


    @GetMapping("/profile-picture/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(new File(".").getCanonicalPath(), "uploads", filename);
        Resource resource = userService.getProfilePicture(filename, filePath);
        String contentType = Files.probeContentType(filePath);
        return ResponseEntity.ok()
                .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                .body(resource);
    }
}


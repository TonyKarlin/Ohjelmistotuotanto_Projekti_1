package backend_api.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import backend_api.DTOs.UpdateUserRequest;
import backend_api.DTOs.UserDTO;
import backend_api.utils.JwtUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend_api.DTOs.LoginRequest;
import backend_api.DTOs.RegisterRequest;
import backend_api.entities.User;
import backend_api.services.UserService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static JwtUtil jwtUtil;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
            user.setProfilePicture("default.png");
            User savedUser = userService.register(user);
            return ResponseEntity.ok(new UserDTO(savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername());
                    Map<String, Object> response = new HashMap<>();
                    response.put("username", user.getUsername());
                    response.put("email", user.getEmail());
                    response.put("id", user.getId());
                    response.put("token", token);
                    return ResponseEntity.ok(response);

                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid username or password")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            if (!JwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String usernameFromToken = JwtUtil.getUsernameFromToken(token);
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            User user = userOptional.get();
            if (!usernameFromToken.equals(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot modify another user");
            }

            if (request.getUsername() != null && !request.getUsername().isBlank()) {
                Optional<User> existing = userService.getAllUsers().stream()
                        .filter(u -> u.getUsername().equals(request.getUsername()) && !u.getId().equals(id))
                        .findAny();
                if (existing.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
                }
                user.setUsername(request.getUsername());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail());
            }

            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(userService.encodePassword(request.getPassword()));
            }

            userService.save(user);

            return ResponseEntity.ok(new UserDTO(user));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }






    @PostMapping("/{id}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            User user = userOptional.get();

            // Server's directory
            String serverDir = new File(".").getCanonicalPath(); // project root
            String uploadDir = serverDir + File.separator + "uploads";

            // Directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Unique filename
            String filename = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(uploadDir + File.separator + filename);
            file.transferTo(destination);

            // Store filename in DB
            user.setProfilePicture(filename);
            userService.save(user);

            return ResponseEntity.ok(new UserDTO(user));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading profile picture: " + e.getMessage());
        }
    }

    @GetMapping("/profile-picture/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(new File(".").getCanonicalPath(), "uploads", filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

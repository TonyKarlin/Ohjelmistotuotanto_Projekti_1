package backend_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend_api.entities.User;
import backend_api.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable long userId) {
        Optional<User> user = repository.findById(userId);
        return user;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return repository.saveAndFlush(user);
    }
}

package backend_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend_api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

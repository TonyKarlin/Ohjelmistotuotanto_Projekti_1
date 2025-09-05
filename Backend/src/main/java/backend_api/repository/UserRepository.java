package backend_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend_api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

package backend_api.repository;

import backend_api.entities.Contacts;
import backend_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContactsRepository extends JpaRepository<Contacts, Long> {
    Optional<Contacts> findByUserAndContact(User user, User contact);
    List<Contacts> findAllByUser(User user);
}

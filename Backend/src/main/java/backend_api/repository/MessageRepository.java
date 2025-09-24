package backend_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend_api.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesByConversationId(Long conversation);

    Optional<Message> findByIdAndConversationId(Long messageId, Long conversationId);

}

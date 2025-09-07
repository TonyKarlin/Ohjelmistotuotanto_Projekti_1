package backend_api.repository;

import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> getMessageByIdAndSender_Id(Long messageId, Long senderId);

    List<Message> findBySender(User sender);

    List<Message> findByConversationId(Conversation conversation);


}

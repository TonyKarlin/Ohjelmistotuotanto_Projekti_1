package backend_api.repository;

import backend_api.entities.Conversation;
import backend_api.entities.Message;
import backend_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> getMessageByIdAndSender_Id(Long messageId, Long senderId);

    List<Message> findMessagesByConversationId(Long conversation);


}

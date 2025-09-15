package backend_api.repository;

import backend_api.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
            SELECT c
            FROM Conversation c
            JOIN c.participants cp1
            JOIN c.participants cp2
            WHERE cp1.user.id = :user1
              AND cp2.user.id = :user2
              AND c.type = 'PRIVATE'
            """)
    Optional<Conversation> findPrivateConversation(@Param("user1") Long user1,
                                                   @Param("user2") Long user2);

}

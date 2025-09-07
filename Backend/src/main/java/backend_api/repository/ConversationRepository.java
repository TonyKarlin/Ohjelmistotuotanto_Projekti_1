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
            JOIN c.participants p1
            JOIN c.participants p2
            WHERE p1.id = :user1
            AND p2.id = :user2
            """)
    Optional<Conversation> findPrivateConversation(@Param("user1") Long user1,
                                                   @Param("user2") Long user2);

    @Query("""
                        SELECT c
                        FROM Conversation c
                        JOIN c.participants p
                        WHERE p.id IN :participants
                        GROUP BY c.id
                        HAVING COUNT(p.id) = :size
                                    AND COUNT(p.id) = (SELECT COUNT(p2.id) FROM c.participants p2)
            
            """)
    Optional<Conversation> findGroupConversation(@Param("participants")List<Long> participantIds,
                                                 @Param("size") long size);
}

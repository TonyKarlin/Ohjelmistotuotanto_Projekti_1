package backend_api.services;

import backend_api.entities.Conversation;
import backend_api.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    // TODO: Implement conversation-related business logic
    public Optional<Conversation> findById(Long id) {
        return conversationRepository.findById(id);
    }
}

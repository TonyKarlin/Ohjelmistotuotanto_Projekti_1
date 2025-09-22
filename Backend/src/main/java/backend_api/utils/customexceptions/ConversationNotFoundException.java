package backend_api.utils.customexceptions;

public class ConversationNotFoundException extends RuntimeException{
    public ConversationNotFoundException(String message) {
        super(message);
    }
}

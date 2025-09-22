package backend_api.utils.customexceptions;

public class PrivateConversationException extends RuntimeException{
    public PrivateConversationException(String message) {
        super(message);
    }
}

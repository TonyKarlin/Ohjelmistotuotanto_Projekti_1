package backend_api.utils.customexceptions;

public class InvalidConversationRequestException extends RuntimeException{
    public InvalidConversationRequestException(String message) {
        super(message);
    }
}

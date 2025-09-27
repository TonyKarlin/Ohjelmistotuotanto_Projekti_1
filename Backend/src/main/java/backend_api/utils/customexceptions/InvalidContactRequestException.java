package backend_api.utils.customexceptions;

public class InvalidContactRequestException extends RuntimeException{
    public InvalidContactRequestException(String message) {
        super(message);
    }
}

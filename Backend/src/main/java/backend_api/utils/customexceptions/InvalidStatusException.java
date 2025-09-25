package backend_api.utils.customexceptions;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message) {
        super(message);
    }
}

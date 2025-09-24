package backend_api.utils.customexceptions;

public class BadMessageRequestException extends RuntimeException{
    public BadMessageRequestException(String message) {
        super(message);
    }
}

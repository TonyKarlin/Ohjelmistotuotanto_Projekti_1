package backend_api.utils.customexceptions;

public class UserAlreadyParticipantException extends RuntimeException{
    public UserAlreadyParticipantException(String message) {
        super(message);
    }
}

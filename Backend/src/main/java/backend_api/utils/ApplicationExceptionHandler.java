package backend_api.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import backend_api.utils.customexceptions.BadMessageRequestException;
import backend_api.utils.customexceptions.ContactAlreadyExistsException;
import backend_api.utils.customexceptions.ContactNotFoundException;
import backend_api.utils.customexceptions.ConversationNotFoundException;
import backend_api.utils.customexceptions.InvalidContactRequestException;
import backend_api.utils.customexceptions.InvalidConversationRequestException;
import backend_api.utils.customexceptions.InvalidStatusException;
import backend_api.utils.customexceptions.InvalidUserException;
import backend_api.utils.customexceptions.MessageNotFoundException;
import backend_api.utils.customexceptions.PrivateConversationException;
import backend_api.utils.customexceptions.UnauthorizedActionException;
import backend_api.utils.customexceptions.UserAlreadyParticipantException;
import backend_api.utils.customexceptions.UserNotFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    // Custom exception handlers for better error responses
    // Steers away from generic RuntimeExceptions in services and
    // gives more specific feedback to the client
    private static final String NOT_FOUND = "Not Found: ";
    private static final String BAD_REQUEST = "Bad Request: ";
    private static final String CONFLICT = "Conflict: ";
    private static final String UNAUTHORIZED = "Unauthorized: ";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }

    @ExceptionHandler(UserAlreadyParticipantException.class)
    public ResponseEntity<String> handleUserAlreadyParticipantException(UserAlreadyParticipantException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CONFLICT + e.getMessage());
    }

    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<String> handleConversationNotFoundException(ConversationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + e.getMessage());
    }

    @ExceptionHandler(PrivateConversationException.class)
    public ResponseEntity<String> handlePrivateConversationException(PrivateConversationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

    @ExceptionHandler(InvalidConversationRequestException.class)
    public ResponseEntity<String> handleInvalidConversationRequestException(InvalidConversationRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedActionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UNAUTHORIZED + e.getMessage());
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<String> handleMessageNotFoundException(MessageNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + e.getMessage());
    }

    @ExceptionHandler(ContactAlreadyExistsException.class)
    public ResponseEntity<String> handleContactAlreadyExistsException(ContactAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CONFLICT + e.getMessage());
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<String> handleContactNotFoundException(ContactNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND + e.getMessage());
    }

    @ExceptionHandler(BadMessageRequestException.class)
    public ResponseEntity<String> handleBadMessageRequestException(BadMessageRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<String> handleInvalidStatusException(InvalidStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

    @ExceptionHandler(InvalidContactRequestException.class)
    public ResponseEntity<String> handleInvalidContactRequestException(InvalidContactRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST + e.getMessage());
    }

}

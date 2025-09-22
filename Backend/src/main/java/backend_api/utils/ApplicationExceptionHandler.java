package backend_api.utils;

import backend_api.utils.customexceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    // Custom exception handlers for better error responses
    // Steers away from generic RuntimeExceptions in services and
    // gives more specific feedback to the client

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Internal Server Error: " + e.getMessage());
    }

    @ExceptionHandler(UserAlreadyParticipantException.class)
    public ResponseEntity<String> handleUserAlreadyParticipant(UserAlreadyParticipantException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + e.getMessage());
    }

    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<String> handleConversationNotFound(ConversationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
    }

    @ExceptionHandler(PrivateConversationException.class)
    public ResponseEntity<String> handlePrivateConversationException(PrivateConversationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + e.getMessage());
    }

    @ExceptionHandler(InvalidConversationRequestException.class)
    public ResponseEntity<String> handleInvalidConversationRequest(InvalidConversationRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: " + e.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedActionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<String> handleMessageNotFound(MessageNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
    }


}


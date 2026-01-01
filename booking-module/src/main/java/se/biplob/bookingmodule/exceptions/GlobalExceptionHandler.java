package se.biplob.bookingmodule.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =========================
       DOMAIN EXCEPTIONS
       ========================= */

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(
            BookingNotFoundException ex
    ) {
        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<ErrorResponse> handleSlotConflict(
            SlotAlreadyBookedException ex
    ) {
        return buildError(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(
            InvalidBookingStateException ex
    ) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    /* =========================
       VALIDATION ERRORS
       ========================= */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildError(message, "VALIDATION_FAILED", HttpStatus.BAD_REQUEST);
    }

    /* =========================
       FALLBACK
       ========================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildError(
                "Internal server error",
                ex.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /* =========================
       HELPER
       ========================= */
    private ResponseEntity<ErrorResponse> buildError(
            Exception ex,
            HttpStatus status
    ) {
        return buildError(ex.getMessage(), ex.getClass().getSimpleName(), status);
    }

    private ResponseEntity<ErrorResponse> buildError(
            String message,
            String cause,
            HttpStatus status
    ) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(message)
                        .cause(cause)
                        .httpStatus(status.value())
                        .timestamp(LocalDateTime.now())
                        .build(),
                status
        );
    }
}


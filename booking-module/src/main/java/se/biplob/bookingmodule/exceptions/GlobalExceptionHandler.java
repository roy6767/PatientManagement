package se.biplob.bookingmodule.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleBookingNotFound(
            BookingNotFoundException ex
    ) {
        return buildError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<ExceptionResponse> handleSlotConflict(
            SlotAlreadyBookedException ex
    ) {
        return buildError(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidBookingStateException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidState(
            InvalidBookingStateException ex
    ) {
        return buildError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(
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
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ExceptionResponse> handleExternalService(
            ExternalServiceException ex
    ) {
        return buildError(
                ex.getMessage(),
                "EXTERNAL_SERVICE_ERROR",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        return buildError(
                "Internal server error",
                ex.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ExceptionResponse> buildError(
            Exception ex,
            HttpStatus status
    ) {
        return buildError(ex.getMessage(), ex.getClass().getSimpleName(), status);
    }

    private ResponseEntity<ExceptionResponse> buildError(
            String message,
            String cause,
            HttpStatus status
    ) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(message)
                        .cause(cause)
                        .status(status)
                        .timestamp(LocalDateTime.now())
                        .build(),
                status
        );
    }
}


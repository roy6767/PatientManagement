package se.biplob.departmentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDoctorNotFound(DoctorNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(TreatmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTreatmentNotFound(TreatmentNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .cause(ex.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .cause(ex.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(response, status);
    }
}


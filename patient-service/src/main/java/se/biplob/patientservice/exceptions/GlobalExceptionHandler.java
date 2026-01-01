package se.biplob.patientservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 2. NEW: Handle Enum/JSON Mismatches (400 - Bad Request)
    // If the frontend sends "GENDER": "invalid", this catches it.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponseDto> handleReadableException(HttpMessageNotReadableException ex) {
        log.warn("Payload error: {}", ex.getMessage());
        return createResponse("Malformed JSON request or invalid field values (check Enums)",
                HttpStatus.BAD_REQUEST, null);
    }

    // 3. NEW: Handle Database Unique Constraints (409 - Conflict)
    // This catches the "Duplicate Email" error if the service check is bypassed.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Database constraint violation: {}", ex.getMessage());
        return createResponse("This record already exists (Duplicate Email/ID)",
                HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleNotFound(PatientNotFoundException ex) {
        return createResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    // 4. The Final Safety Net (500 - Internal Server Error)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionResponseDto> handleGeneral(Exception ex) {
//        log.error("CRITICAL ERROR: ", ex); // We log the real error for us
//        return createResponse("An unexpected server error occurred",
//                HttpStatus.INTERNAL_SERVER_ERROR, "Contact support");
//    }

    private ResponseEntity<ExceptionResponseDto> createResponse(String msg, HttpStatus status, String detail) {
        return new ResponseEntity<>(new ExceptionResponseDto(LocalDateTime.now(), msg, detail, status), status);
    }
}
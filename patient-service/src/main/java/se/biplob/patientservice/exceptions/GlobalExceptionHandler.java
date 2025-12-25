package se.biplob.patientservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map <String,String> error = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                err -> {
                    String fieldName = ((FieldError)err).getField();
                    String errorMessage = err.getDefaultMessage();
                    error.put(fieldName,errorMessage);
                });
        return error;
    }
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handlePatientNotFoundException(PatientNotFoundException ex){
        ExceptionResponseDto payload = new ExceptionResponseDto(
                LocalDateTime.now(),
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(payload, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleAllExceptions(Exception ex) {
        ExceptionResponseDto payload = new ExceptionResponseDto(
                LocalDateTime.now(),
                "An unexpected error occurred",
                ex.getCause() != null ? ex.getCause().toString() : ex.toString(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

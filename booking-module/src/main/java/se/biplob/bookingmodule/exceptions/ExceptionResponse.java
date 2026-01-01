package se.biplob.bookingmodule.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private String message;
    private LocalDateTime timestamp;
    private String cause;
    private HttpStatus status;
}

package se.biplob.bookingmodule.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import se.biplob.bookingmodule.model.Enum.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class BookingResponse {

    private UUID id;
    private UUID patientId;
    private Long doctorId;
    private Long treatmentId;

    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private BookingStatus status;
    private LocalDateTime createdAt;
}


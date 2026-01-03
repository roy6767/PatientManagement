package se.biplob.billingservice.kafka.event;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {

    private UUID bookingId;
    private UUID patientId;
    private Long doctorId;
    private Long treatmentId;

    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private Double amount;
}

package se.biplob.bookingmodule.dtos.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "PATIENT_ID_REQUIRED")
    private UUID patientId;

    @NotNull(message = "DOCTOR_ID_REQUIRED")
    private Long doctorId;

    @NotNull(message = "TREATMENT_ID_REQUIRED")
    private Long treatmentId;

    @NotNull(message = "APPOINTMENT_DATE_REQUIRED")
    @FutureOrPresent(message = "INVALID_APPOINTMENT_DATE")
    private LocalDate appointmentDate;

    @NotNull(message = "START_TIME_REQUIRED")
    private LocalTime startTime;

    @NotNull(message = "END_TIME_REQUIRED")
    private LocalTime endTime;
}


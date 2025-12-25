package se.biplob.patientservice.dto;

import jakarta.validation.constraints.*;
import se.biplob.patientservice.model.enums.Gender;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank(message = "NAME_REQUIRED")
        String name,

        @Email(message = "INVALID_EMAIL")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$")
        String email,

        @PastOrPresent(message = "INVALID_BIRTHDATE")
        LocalDate birthDate,

        @NotNull(message = "ADDRESS_REQUIRED")
        String address,

        Gender gender,
        String phoneNumber
) {}
package se.biplob.patientservice.dto;

import se.biplob.patientservice.model.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String name,
        String email,
        LocalDate birthDate,
        LocalDate registeredDate,
        Gender gender,
        String phoneNumber
) {}
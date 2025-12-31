package se.biplob.bookingmodule.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private UUID id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private LocalDate registeredDate;
    private Gender gender;
    private String phoneNumber;
}

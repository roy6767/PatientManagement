package se.biplob.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientsResponseDto {
    private String name;
    private String email;
    private String address;
    private LocalDate birthDate;
    private LocalDate registeredDate;
    private String gender;
    private String phoneNumber;
}

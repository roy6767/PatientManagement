package se.biplob.patientservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientsRequestDto {
    @NotBlank(message="Name is required")
    private String name;
    @PositiveOrZero(message="Age can not be negative value")
    @Size(max=100, message= "Age limit is 100 years old")
    private int age;
    @NotBlank
    @Email
    @Email(message="Its not a valid email address")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", //here {2,} defines atleast two letters like .co, .se, .com but not .c
            flags = Pattern.Flag.CASE_INSENSITIVE, // this one allows capital letters like HITRACT.COM or Hitract.Com
            message = "Email must contain a valid domain like .com, .org, .se, etc."
    )
    private String email;
    @NotNull
    private String address;
    @PastOrPresent
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
}

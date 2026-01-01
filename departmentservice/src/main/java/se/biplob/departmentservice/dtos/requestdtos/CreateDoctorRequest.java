package se.biplob.departmentservice.dtos.requestdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDoctorRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String designation;
    private String about;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Long departmentId;
}


package se.biplob.departmentservice.dtos.requestdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTreatmentRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long departmentId;

    @NotNull

    private double price;

}

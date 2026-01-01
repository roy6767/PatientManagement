package se.biplob.departmentservice.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TreatmentResponse {

    private Long id;
    private String name;
    private double price;
}

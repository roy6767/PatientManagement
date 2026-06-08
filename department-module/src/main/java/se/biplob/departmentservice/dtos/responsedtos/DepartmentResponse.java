package se.biplob.departmentservice.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
}

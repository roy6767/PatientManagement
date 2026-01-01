package se.biplob.departmentservice.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DepartmentDetailsResponse {

    private DepartmentResponse department;
    private List<DoctorResponse> doctors;
    private List<TreatmentResponse> treatments;
}


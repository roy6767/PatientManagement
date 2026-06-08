package se.biplob.departmentservice.dtos.responsedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DoctorWithTreatmentsResponse {

    private DoctorResponse doctor;
    private List<TreatmentResponse> treatments;
}

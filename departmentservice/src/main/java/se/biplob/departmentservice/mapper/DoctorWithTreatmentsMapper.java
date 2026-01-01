package se.biplob.departmentservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.departmentservice.dtos.responsedtos.DoctorWithTreatmentsResponse;
import se.biplob.departmentservice.model.Doctor;
import se.biplob.departmentservice.model.Treatment;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, TreatmentMapper.class})
public interface DoctorWithTreatmentsMapper {

    DoctorWithTreatmentsResponse toResponse(
            Doctor doctor,
            List<Treatment> treatments
    );
}


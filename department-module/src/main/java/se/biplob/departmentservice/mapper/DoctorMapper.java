package se.biplob.departmentservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.departmentservice.dtos.requestdtos.CreateDoctorRequest;
import se.biplob.departmentservice.dtos.responsedtos.DoctorResponse;
import se.biplob.departmentservice.model.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResponse toResponse(Doctor doctor);

    Doctor toEntity(CreateDoctorRequest request);
}

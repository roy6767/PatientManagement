package se.biplob.departmentservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.departmentservice.dtos.responsedtos.DepartmentDetailsResponse;
import se.biplob.departmentservice.model.Department;
import se.biplob.departmentservice.model.Doctor;
import se.biplob.departmentservice.model.Treatment;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, TreatmentMapper.class})
public interface DepartmentDetailsMapper {

    DepartmentDetailsResponse toResponse(
            Department department,
            List<Doctor> doctors,
            List<Treatment> treatments
    );
}

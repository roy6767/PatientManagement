package se.biplob.departmentservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.departmentservice.dtos.requestdtos.CreateDepartmentRequest;
import se.biplob.departmentservice.dtos.responsedtos.DepartmentResponse;
import se.biplob.departmentservice.model.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentResponse toResponse(Department department);

    Department toEntity(CreateDepartmentRequest request);
}

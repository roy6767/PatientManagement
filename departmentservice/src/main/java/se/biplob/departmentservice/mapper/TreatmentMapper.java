package se.biplob.departmentservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.departmentservice.dtos.requestdtos.CreateTreatmentRequest;
import se.biplob.departmentservice.dtos.responsedtos.TreatmentResponse;
import se.biplob.departmentservice.model.Treatment;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    TreatmentResponse toResponse(Treatment treatment);

    Treatment toEntity(CreateTreatmentRequest request);
}

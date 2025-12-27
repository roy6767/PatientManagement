package se.biplob.patientservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import se.biplob.patientservice.dto.PatientRequest;
import se.biplob.patientservice.dto.PatientResponse;
import se.biplob.patientservice.model.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toResponse(Patient entity);
    Patient toEntity(PatientRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    void updateEntityFromDto(PatientRequest dto, @MappingTarget Patient entity);
}
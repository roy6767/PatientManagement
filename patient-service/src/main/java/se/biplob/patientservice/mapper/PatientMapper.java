package se.biplob.patientservice.mapper;

import org.mapstruct.Mapper;
import se.biplob.patientservice.dto.PatientRequest;
import se.biplob.patientservice.dto.PatientResponse;
import se.biplob.patientservice.model.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientResponse toResponse(Patient entity);
    Patient toEntity(PatientRequest dto);
}
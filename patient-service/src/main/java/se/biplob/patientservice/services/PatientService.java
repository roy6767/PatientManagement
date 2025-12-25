package se.biplob.patientservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.biplob.patientservice.dto.PatientRequest;
import se.biplob.patientservice.dto.PatientResponse;
import se.biplob.patientservice.exceptions.DuplicateResourceException;
import se.biplob.patientservice.exceptions.PatientNotFoundException;
import se.biplob.patientservice.mapper.PatientMapper;
import se.biplob.patientservice.model.Patient;
import se.biplob.patientservice.repositories.PatientRepository;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper mapper;

    @Transactional(readOnly = true)
    public Page<PatientResponse> getPatients(String searchTerm, Pageable pageable) {
        Page<Patient> patients;

        if (searchTerm != null && !searchTerm.isBlank()) {
            patients = patientRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        } else {
            patients = patientRepository.findAll(pageable);
        }

        return patients.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(UUID id) {
        return patientRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found: " + id));
    }

    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already in use");
        }
        Patient patient = mapper.toEntity(request);
        return mapper.toResponse(patientRepository.save(patient));
    }
}
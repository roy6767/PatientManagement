package se.biplob.patientservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.biplob.patientservice.model.Patient;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
}